package org.fenixsoft.hotspot.model;

import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.fenixsoft.hotspot.Activator;

/**
 * HotSpot参数内容提供者，供TreeViewer列表显示，以及ViewFilter过滤使用
 * 
 * @author icyfenix@gmail.com
 */
public class HotSpotArgumentsContentProvider implements ITreeContentProvider, ITableLabelProvider, ICheckStateProvider {

	/** 文件夹和参数图片 */
	private Image categoryImage = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/prj_obj.gif");
	private Image argumentImage = ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/java.gif");

	/** 参数仓库 */
	private ArgumentRepostory repostory;

	public ArgumentRepostory getRepostory() {
		return repostory;
	}

	public void setRepostory(ArgumentRepostory repostory) {
		this.repostory = repostory;
	}

	/**
	 * 返回传入对象的所有子对象，如果该对象属于叶子节点没有子对象，返回null
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof String) {
			return repostory.getArgumentsByCategory((String) parentElement).toArray();
		} else if (parentElement instanceof Argument) {
			return null;
		} else {
			throw new IllegalAccessError("never get here");
		}

	}

	/**
	 * 返回传入对象的父对象
	 */
	@Override
	public Object getParent(Object element) {
		if (element instanceof String) {
			return repostory;
		} else if (element instanceof Argument) {
			return ((Argument) element).getFirstCategory();
		} else {
			throw new IllegalAccessError("never get here");
		}
	}

	/**
	 * 判断传入对象是否具有子对象
	 */
	@Override
	public boolean hasChildren(Object element) {
		return element instanceof String && repostory.getArgumentsByCategory((String) element) != null;
	}

	/**
	 * 根据输入的内容（由TreeViewer的input方法提供）获取该元素的所有子元素。<br>
	 * 这里输入内容必须是ArgumentRepostory的实例
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		repostory = (ArgumentRepostory) inputElement;
		return repostory.getAllArgumentsWithCateorgy().keySet().toArray();
	}

	/**
	 * 获取制定列显示图标
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex == 0) {
			if (element instanceof String) {
				return categoryImage;
			} else {
				return argumentImage;
			}
		} else {
			return null;
		}
	}

	/**
	 * 获取指定列的显示内容
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof String) {
			// Category节点只在第一列显示名称
			if (columnIndex == 0) {
				return (String) element;
			} else {
				return "";
			}
		} else {
			// 直接点各列显示内容
			Argument arg = (Argument) element;
			switch (columnIndex) {
			case 0:
				return arg.getName();
			case 1:
				return arg.getArgumentType().toString();
			case 2:
				return arg.getDataType();
			case 3:
				return arg.getDefaultValue();
			case 4:
				return arg.getUserValue();
			case 5:
				return arg.getComment();
			default:
				return "";
			}
		}
	}

	/**
	 * 是否选中
	 */
	@Override
	public boolean isChecked(Object element) {
		if (element instanceof Argument) {
			return ((Argument) element).isCheck();
		} else {
			return false;
		}
	}

	/**
	 * 是否置灰
	 */
	@Override
	public boolean isGrayed(Object element) {
		if (element instanceof Argument) {
			return false;
		} else {
			return true;
		}
	}

	// ////////////////////////////
	// 以下为不需要使用的方法
	// ////////////////////////////

	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}
}
