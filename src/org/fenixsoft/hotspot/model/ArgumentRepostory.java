package org.fenixsoft.hotspot.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HotSpot虚拟机默认值数据仓库
 * 
 * @author icyfenix@gmail.com
 */
public class ArgumentRepostory {

	/** 参数分类索引 */
	private Map<String, List<Argument>> arguments = new HashMap<String, List<Argument>>();
	/** 参数索引，根据参数名称进行全局查找 */
	private Map<String, Argument> argumentsIndex = new HashMap<String, Argument>();

	public Map<String, Argument> getArgumentIndex() {
		return argumentsIndex;
	}

	public Map<String, List<Argument>> getAllArgumentsWithCateorgy() {
		return arguments;
	}

	public List<Argument> getArgumentsByCategory(String category) {
		return arguments.get(category);
	}

	/**
	 * 新增参数到仓库中
	 * 
	 * @param arg
	 */
	public void addArgument(Argument arg) {
		String[] categories = arg.getCategory().split("/");
		for (String category : categories) {
			List<Argument> args = arguments.get(category);
			if (args == null) {
				args = new ArrayList<Argument>();
				arguments.put(category, args);
			}
			args.add(arg);
		}
		argumentsIndex.put(arg.getName(), arg);
	}

	/**
	 * 默认从HotSpotDefaultArguments.txt文件中初始化参数信息
	 */
	public ArgumentRepostory() {
		try {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("HotSpotDefaultArguments.txt")));
				String line = null;
				while ((line = in.readLine()) != null) {
					addArgument(new Argument(line));
				}
			} finally {
				if (in != null) {
					in.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
