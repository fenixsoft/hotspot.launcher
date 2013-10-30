package org.fenixsoft.hotspot.model;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * 根据参数类型过滤内容， HotSpot参数类型包括：<br>
 * develop, develop_pd, product, product_pd, diagnostic, experimental,
 * notproduct, manageable, product_rw, lp64_product,standard,custom <br>
 * 默认仅显示Product参数
 * 
 * @author icyfenix@gmail.com
 */
public class HotSpotArgumentsContentArgumentTypeFilter extends ViewerFilter {

	private boolean selectProductFlag = true;
	private boolean selectManageableFlag = false;
	private boolean selectDiagnosticFlag = false;
	private boolean selectExperimentalFlag = false;
	private boolean selectNotproductFlag = false;
	private boolean selectProductRWFlag = false;
	private boolean selectDevelopFlag = false;
	private boolean selectStandardFlag = true;
	private boolean selectCustomFlag = true;

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof Argument) {
			ArgumentType type = ((Argument) element).getArgumentType();
			boolean display = false;
			if (selectProductFlag && !display) {
				display = (type == ArgumentType.product || type == ArgumentType.product_pd || type == ArgumentType.lp64_product);
			}
			if (selectManageableFlag && !display) {
				display = (type == ArgumentType.manageable);
			}
			if (selectDiagnosticFlag && !display) {
				display = (type == ArgumentType.diagnostic);
			}
			if (selectExperimentalFlag && !display) {
				display = (type == ArgumentType.experimental);
			}
			if (selectNotproductFlag && !display) {
				display = (type == ArgumentType.notproduct);
			}
			if (selectProductRWFlag && !display) {
				display = (type == ArgumentType.product_rw);
			}
			if (selectDevelopFlag && !display) {
				display = (type == ArgumentType.develop || type == ArgumentType.develop_pd);
			}
			if (selectStandardFlag && !display) {
				display = (type == ArgumentType.standard);
			}
			if (selectCustomFlag && !display) {
				display = (type == ArgumentType.custom);
			}
			return display;
		} else {
			return true;
		}
	}

	public boolean isSelectProductFlag() {
		return selectProductFlag;
	}

	public void setSelectProductFlag(boolean selectProductFlag) {
		this.selectProductFlag = selectProductFlag;
	}

	public boolean isSelectManageableFlag() {
		return selectManageableFlag;
	}

	public void setSelectManageableFlag(boolean selectManageableFlag) {
		this.selectManageableFlag = selectManageableFlag;
	}

	public boolean isSelectDiagnosticFlag() {
		return selectDiagnosticFlag;
	}

	public void setSelectDiagnosticFlag(boolean selectDiagnosticFlag) {
		this.selectDiagnosticFlag = selectDiagnosticFlag;
	}

	public boolean isSelectExperimentalFlag() {
		return selectExperimentalFlag;
	}

	public void setSelectExperimentalFlag(boolean selectExperimentalFlag) {
		this.selectExperimentalFlag = selectExperimentalFlag;
	}

	public boolean isSelectNotproductFlag() {
		return selectNotproductFlag;
	}

	public void setSelectNotproductFlag(boolean selectNotproductFlag) {
		this.selectNotproductFlag = selectNotproductFlag;
	}

	public boolean isSelectProductRWFlag() {
		return selectProductRWFlag;
	}

	public void setSelectProductRWFlag(boolean selectProductRWFlag) {
		this.selectProductRWFlag = selectProductRWFlag;
	}

	public boolean isSelectDevelopFlag() {
		return selectDevelopFlag;
	}

	public void setSelectDevelopFlag(boolean selectDevelopFlag) {
		this.selectDevelopFlag = selectDevelopFlag;
	}

	public boolean isSelectStandardFlag() {
		return selectStandardFlag;
	}

	public void setSelectStandardFlag(boolean selectStandardFlag) {
		this.selectStandardFlag = selectStandardFlag;
	}

	public boolean isSelectCustomFlag() {
		return selectCustomFlag;
	}

	public void setSelectCustomFlag(boolean selectCustomFlag) {
		this.selectCustomFlag = selectCustomFlag;
	}

}
