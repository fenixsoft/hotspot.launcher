package org.fenixsoft.hotspot.model;

import java.util.Map;

/**
 * 根据参数仓库中用户选择的内容，生成HotSpot参数，或者根据HotSpot参数内容，产生参数仓库中的设置选项
 * 
 * @author icyfenix@gmail.com
 */
public class ArgumentCodeTranslater {

	/**
	 * 根据参数仓库中用户设置内容生成HotSpot参数字符串
	 */
	public String getArgumentCodeStringFromRepostory(ArgumentRepostory repo) {
		StringBuffer ret = new StringBuffer();
		for (Map.Entry<String, Argument> argumentEntity : repo.getArgumentIndex().entrySet()) {
			Argument argument = argumentEntity.getValue();
			if (argument.isCheck()) {
				String name = argument.getName();
				String value = argument.getUserValue();
				value = value == null ? "" : value;
				if (argument.getArgumentType() == ArgumentType.standard) {
					// 标准参数
					if ("Xmx".equals(name) || "Xms".equals(name) || "Xss".equals(name) || "Xmn".equals(name)) {
						ret.append(" -").append(name).append(value);
					} else {
						if ("".equals(value)) {
							ret.append(" -").append(name);
						} else {
							ret.append(" -").append(name).append(":").append(value);
						}
					}
				} else if (argument.getArgumentType() == ArgumentType.custom) {
					// 用户参数
					ret.append(" -D").append(name).append("=").append(value);
				} else {
					// 非稳定参数
					ret.append(" -XX:");
					if ("bool".equals(argument.getDataType())) {
						ret.append(Boolean.valueOf(value) ? "+" : "-").append(name);
					} else {
						ret.append(name).append("=").append(value);
					}
				}
			}
		}
		return ret.toString().trim();
	}

	/**
	 * 根据HotSpot参数字符串设定参数仓库中用户设置内容
	 */
	public void setArgumentCodeStringToRepostory(ArgumentRepostory repo, String codeString) {
		if (codeString == null || codeString.trim().length() == 0) {
			return;
		}
		String[] codes = codeString.split(" ");
		String name = "", value = "";
		boolean isCustom;
		for (String code : codes) {
			if (code == null || code.trim().length() == 0) {
				continue;
			}
			try {
				isCustom = false;
				if (code.startsWith("-XX:")) {
					// 非稳定参数
					code = code.substring(4);
					if (code.startsWith("+")) {
						name = code.substring(1);
						value = "true";
					} else if (code.startsWith("-")) {
						name = code.substring(1);
						value = "false";
					} else {
						int equalMarkIndex = code.indexOf("=");
						name = code.substring(0, equalMarkIndex);
						value = code.substring(equalMarkIndex + 1);
					}
				} else if (code.startsWith("-D")) {
					// 用户参数
					isCustom = true;
					code = code.substring(2);
					int equalMarkIndex = code.indexOf("=");
					if (equalMarkIndex > -1) {
						name = code.substring(0, equalMarkIndex);
						value = code.substring(equalMarkIndex + 1);
					} else {
						name = code;
						value = "";
					}
				} else {
					// 标准参数
					code = code.substring(1);
					if (code.startsWith("Xmx") || code.startsWith("Xms") || code.startsWith("Xss") || code.startsWith("Xmn")) {
						name = code.substring(0, 3);
						value = code.substring(3);
					} else {
						int equalMarkIndex = code.indexOf(":");
						if (equalMarkIndex > -1) {
							name = code.substring(0, equalMarkIndex);
							value = code.substring(equalMarkIndex + 1);
						} else {
							name = code;
							value = "";
						}
					}
				}
				// 回写仓库
				Argument arg = repo.getArgumentIndex().get(name);
				if (arg == null) {
					// 如果该参数在仓库中不存在，则在Custom/Unknown分类中新增一个
					arg = new Argument();
					arg.setName(name);
					if (isCustom) {
						arg.setCategory("Custom");
						arg.setArgumentType(ArgumentType.custom);
					} else {
						arg.setCategory("Unknown");
						arg.setArgumentType(ArgumentType.unknown);
					}
					repo.addArgument(arg);
				}
				arg.setUserValue(value);
				arg.setCheck(true);
			} catch (Exception e) {
				// 某个参数解析失败，不能影响其他参数正常解析
				e.printStackTrace();
			}
		}
	}
}
