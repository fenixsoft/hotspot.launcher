package org.fenixsoft.hotspot.ui;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.fenixsoft.hotspot.Activator;
import org.fenixsoft.hotspot.model.HotSpotArgumentsContentArgumentTypeFilter;

/**
 * 过滤参数类型对话框，由WindowBuilder Pro自动生成，避免手工编辑
 * 
 * @author icyfenix@gmail.com
 * 
 */
public class ArgumentTypeFilterDetailsDialog extends Dialog {
	protected DataBindingContext m_bindingContext;

	private HotSpotArgumentsContentArgumentTypeFilter filter;
	private HotSpotArgumentsComposite composite;
	private Button chk_product;
	private Button chk_manageable;
	private Button chk_experimental;
	private Button chk_diagnostic;
	private Button chk_notproduct;
	private Button chk_product_rw;
	private Button chk_develop;
	private Button chk_standard;
	private Button chk_custom;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ArgumentTypeFilterDetailsDialog(Shell parentShell, HotSpotArgumentsContentArgumentTypeFilter filter, HotSpotArgumentsComposite composite) {
		super(parentShell);
		this.filter = filter;
		this.composite = composite;
	}

	@Override
	protected void configureShell(Shell shell) {
		setDefaultImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/datasheet.gif"));
		shell.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/datasheet.gif"));
		super.configureShell(shell);
		shell.setText("Type Filter");
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		FillLayout fl_container = new FillLayout(SWT.HORIZONTAL);
		fl_container.marginWidth = 4;
		fl_container.marginHeight = 4;
		container.setLayout(fl_container);

		Group grpArgumentType = new Group(container, SWT.NONE);
		grpArgumentType.setText("Select argument types");
		GridLayout gl_grpArgumentType = new GridLayout(1, false);
		gl_grpArgumentType.marginWidth = 10;
		gl_grpArgumentType.marginHeight = 10;
		grpArgumentType.setLayout(gl_grpArgumentType);

		chk_standard = new Button(grpArgumentType, SWT.CHECK);
		chk_standard.setText("Standard (JVM standard options)");

		chk_custom = new Button(grpArgumentType, SWT.CHECK);
		chk_custom.setText("Custom (User defined system properties)");

		chk_product = new Button(grpArgumentType, SWT.CHECK);
		chk_product.setText("Product (flags are always settable / visible)");

		chk_manageable = new Button(grpArgumentType, SWT.CHECK);
		chk_manageable.setText("Manageable (flags are writeable external product flags, through the JMX Bean)");

		chk_product_rw = new Button(grpArgumentType, SWT.CHECK);
		chk_product_rw.setText("Product_RW (flags are writeable internal product flags, may be changed/removed in a future release)");

		chk_experimental = new Button(grpArgumentType, SWT.CHECK);
		chk_experimental.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		chk_experimental.setText("Experimental (flags are experimental ,not part of the officially supported product, need UnlockExperimentalVMOptions)");

		chk_develop = new Button(grpArgumentType, SWT.CHECK);
		chk_develop.setText("Develop (flags are settable / visible only during development and are constant in the PRODUCT version)");

		chk_diagnostic = new Button(grpArgumentType, SWT.CHECK);
		chk_diagnostic.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		chk_diagnostic.setText("Diagnostic (flags are used for diagnosis of VM bugs or quality assurance, need UnlockDiagnosticVMOptions in product version)");

		chk_notproduct = new Button(grpArgumentType, SWT.CHECK);
		chk_notproduct.setText("NotProduct (flags are settable / visible only during development and are not declared in the PRODUCT version)");

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				composite.getCheckboxTreeViewer().refresh();
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		m_bindingContext = initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(801, 334);
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeSelectionBtnCheckButtonObserveWidget = WidgetProperties.selection().observe(chk_product);
		IObservableValue selectProductFlagFilterObserveValue = PojoProperties.value("selectProductFlag").observe(filter);
		bindingContext.bindValue(observeSelectionBtnCheckButtonObserveWidget, selectProductFlagFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionChk_manageableObserveWidget = WidgetProperties.selection().observe(chk_manageable);
		IObservableValue selectManageableFlagFilterObserveValue = PojoProperties.value("selectManageableFlag").observe(filter);
		bindingContext.bindValue(observeSelectionChk_manageableObserveWidget, selectManageableFlagFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionChk_experimentalObserveWidget = WidgetProperties.selection().observe(chk_experimental);
		IObservableValue selectExperimentalFlagFilterObserveValue = PojoProperties.value("selectExperimentalFlag").observe(filter);
		bindingContext.bindValue(observeSelectionChk_experimentalObserveWidget, selectExperimentalFlagFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionChk_diagnosticObserveWidget = WidgetProperties.selection().observe(chk_diagnostic);
		IObservableValue selectDiagnosticFlagFilterObserveValue = PojoProperties.value("selectDiagnosticFlag").observe(filter);
		bindingContext.bindValue(observeSelectionChk_diagnosticObserveWidget, selectDiagnosticFlagFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionChk_notproductObserveWidget = WidgetProperties.selection().observe(chk_notproduct);
		IObservableValue selectNotproductFlagFilterObserveValue = PojoProperties.value("selectNotproductFlag").observe(filter);
		bindingContext.bindValue(observeSelectionChk_notproductObserveWidget, selectNotproductFlagFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionChk_product_rwObserveWidget = WidgetProperties.selection().observe(chk_product_rw);
		IObservableValue selectProductRWFlagFilterObserveValue = PojoProperties.value("selectProductRWFlag").observe(filter);
		bindingContext.bindValue(observeSelectionChk_product_rwObserveWidget, selectProductRWFlagFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionChk_developObserveWidget = WidgetProperties.selection().observe(chk_develop);
		IObservableValue selectDevelopFlagFilterObserveValue = PojoProperties.value("selectDevelopFlag").observe(filter);
		bindingContext.bindValue(observeSelectionChk_developObserveWidget, selectDevelopFlagFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionChk_standardObserveWidget = WidgetProperties.selection().observe(chk_standard);
		IObservableValue selectStandardFlagFilterObserveValue = PojoProperties.value("selectStandardFlag").observe(filter);
		bindingContext.bindValue(observeSelectionChk_standardObserveWidget, selectStandardFlagFilterObserveValue, null, null);
		//
		IObservableValue observeSelectionChk_customObserveWidget = WidgetProperties.selection().observe(chk_custom);
		IObservableValue selectCustomFlagFilterObserveValue = PojoProperties.value("selectCustomFlag").observe(filter);
		bindingContext.bindValue(observeSelectionChk_customObserveWidget, selectCustomFlagFilterObserveValue, null, null);
		//
		return bindingContext;
	}
}
