package org.fenixsoft.hotspot.ui;

import java.lang.reflect.Field;

import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.fenixsoft.hotspot.Activator;
import org.fenixsoft.hotspot.model.ArgumentCodeTranslater;
import org.fenixsoft.hotspot.model.ArgumentRepostory;
import org.fenixsoft.hotspot.model.HotSpotArgumentsContentProvider;

/**
 * 扩展Eclipse原生的LocalJavaApplicationTab中的JavaArgumentsTab，增加针对HotSpot的参数设置页签
 * 
 * @author icyfenix@gmail.com
 */
public class HotSpotJavaArgumentsTab extends JavaArgumentsTab {

	/** 参数文本<->参数仓库对象的双向转换器 */
	private ArgumentCodeTranslater translater = new ArgumentCodeTranslater();
	/** HotSpot参数Composite */
	private HotSpotArgumentsComposite hsaComposite;

	/**
	 * 创建页签控件
	 */
	@Override
	public void createControl(Composite parent) {
		// 主控件区域，是一个CTabFolder
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		final CTabFolder tabFolder = new CTabFolder(mainComposite, SWT.FLAT | SWT.BOTTOM);

		// 调用父类createControl方法，创建原有的JavaArgumentsTab页签控件，并作为basicTabPage页签中的控件
		Composite originalComposite = new Composite(tabFolder, SWT.NONE);
		originalComposite.setLayout(new GridLayout(1, false));
		super.createControl(originalComposite);
		final CTabItem basicTabPage = new CTabItem(tabFolder, SWT.NONE);
		basicTabPage.setText("Basic");
		basicTabPage.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/basic.png"));
		basicTabPage.setControl(originalComposite);

		// 延迟创建HotSpotArgumentsComposite，只生成advanceTabPage页签
		final CTabItem advanceTabPage = new CTabItem(tabFolder, SWT.NONE);
		advanceTabPage.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/advance.png"));
		advanceTabPage.setText("Advance");

		tabFolder.addSelectionListener(new SelectionAdapter() {
			/** 处理两个Tab页签切换事件 */
			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				if (selectionevent.item == advanceTabPage) {
					// Basic页签转向Advance页签，将VM参数文本框的内容解析为已选项
					ArgumentRepostory repo = new ArgumentRepostory();
					String argCode = getVMArgumentText().getText();
					translater.setArgumentCodeStringToRepostory(repo, argCode);
					// 根据新参数创建参数Composite
					hsaComposite = new HotSpotArgumentsComposite(tabFolder, SWT.FULL_SELECTION, repo);
					advanceTabPage.setControl(hsaComposite);
				} else {
					// Advance页签转向Basic页签，将参数设置到VM参数文本框
					String argCode = translater.getArgumentCodeStringFromRepostory(getArgumentRepostory());
					Text text = getVMArgumentText();
					text.setText(argCode);
				}
			}
		});

		tabFolder.setSelection(0);
		setControl(mainComposite);
	}

	/**
	 * 获取当前的虚拟机参数仓库
	 * 
	 * @return
	 */
	private ArgumentRepostory getArgumentRepostory() {
		return ((HotSpotArgumentsContentProvider) hsaComposite.getCheckboxTreeViewer().getContentProvider()).getRepostory();
	}

	/**
	 * 获取虚拟机参数的设置文本框，由于可见性不足，需要反射
	 * 
	 * @return
	 */
	private Text getVMArgumentText() {
		try {
			Field vmTextField = fVMArgumentsBlock.getClass().getDeclaredField("fVMArgumentsText");
			vmTextField.setAccessible(true);
			return (Text) vmTextField.get(fVMArgumentsBlock);
		} catch (Exception e) {
			throw new IllegalAccessError(e.getMessage());
		}
	}

	@Override
	public String getName() {
		return "HS Arguments";
	}

}
