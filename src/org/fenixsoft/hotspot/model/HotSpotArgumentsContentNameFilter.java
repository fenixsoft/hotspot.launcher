package org.fenixsoft.hotspot.model;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Text;

/**
 * 根据用户在过滤控件中输入的内容，在参数名称中进行过滤，大小写不敏感
 * 
 * @author icyfenix@gmail.com
 */
public class HotSpotArgumentsContentNameFilter extends ViewerFilter {

	private Text text;

	public HotSpotArgumentsContentNameFilter(Text text) {
		this.text = text;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof Argument) {
			return ((Argument) element).getName().toUpperCase().indexOf(text.getText().toUpperCase()) > -1;
		} else {
			return true;
		}
	}
}
