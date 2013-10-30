package org.fenixsoft.hotspot.model;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * 根据用户是否选中参数，在参数仓库中进行过滤
 * 
 * @author icyfenix@gmail.com
 */
public class HotSpotArgumentsContentCheckedFilter extends ViewerFilter {

	public HotSpotArgumentsContentCheckedFilter() {
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof Argument) {
			return ((Argument) element).isCheck();
		} else {
			return true;
		}
	}
}
