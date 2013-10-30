package org.fenixsoft.hotspot.ui;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;
import org.fenixsoft.hotspot.Activator;
import org.fenixsoft.hotspot.model.Argument;
import org.fenixsoft.hotspot.model.ArgumentRepostory;
import org.fenixsoft.hotspot.model.HotSpotArgumentsContentArgumentTypeFilter;
import org.fenixsoft.hotspot.model.HotSpotArgumentsContentCheckedFilter;
import org.fenixsoft.hotspot.model.HotSpotArgumentsContentNameFilter;
import org.fenixsoft.hotspot.model.HotSpotArgumentsContentProvider;
import org.fenixsoft.hotspot.model.HotSpotArgumnetsContentSorter;

/**
 * HotSpot参数设置组件Composite
 * 
 * @author icyfenix@gmail.com
 */
public class HotSpotArgumentsComposite extends Composite {

	private final class TreeViewerEditingSupporter extends EditingSupport {
		private TreeViewerEditingSupporter(ColumnViewer viewer) {
			super(viewer);
		}

		/** 该列是否允许编辑 */
		protected boolean canEdit(Object element) {
			if (element instanceof Argument) {
				return ((Argument) element).isCheck();
			} else {
				return false;
			}
		}

		/** 该列的编辑器类型 */
		protected CellEditor getCellEditor(Object element) {
			if ("bool".equals(((Argument) element).getDataType())) {
				return new CheckboxCellEditor((Composite) getViewer().getControl(), SWT.NONE);
			} else {
				return new TextCellEditor((Composite) getViewer().getControl(), SWT.NONE);
			}
		}

		/** 该列的编辑初始值 */
		protected Object getValue(Object element) {
			Argument arg = (Argument) element;
			if ("bool".equals(arg.getDataType())) {
				return Boolean.valueOf(arg.getUserValue());
			} else {
				return arg.getUserValue() == null ? "" : arg.getUserValue();
			}
		}

		/** 编辑后赋值方法 */
		protected void setValue(Object element, Object value) {
			Argument arg = (Argument) element;
			arg.setUserValue(value.toString());
			checkboxTreeViewer.refresh();
		}
	}

	private final class TreeViewerSelectionListener extends SelectionAdapter {
		/** 选中参数时，在commentLabel控件中显示参数注释内容 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			TreeItem[] items = ((Tree) e.getSource()).getSelection();
			if (items != null && items.length > 0) {
				// 多选时显示最后一个参数的注释
				int index = items.length - 1;
				Object data = items[index].getData();
				if (data instanceof Argument) {
					commentLabel.setText(((Argument) data).getComment());
					commentLabel.setToolTipText(((Argument) data).getComment());
				}
			}
		}
	}

	private final class TreeViewerCheckStateListener implements ICheckStateListener {
		/** 当在树表格中选中参数项目时，同步到项目的模型中 */
		@Override
		public void checkStateChanged(CheckStateChangedEvent event) {
			Object ele = event.getElement();
			if (ele instanceof Argument) {
				Argument arg = (Argument) ele;
				arg.setCheck(event.getChecked());
				if (event.getChecked()) {
					if ("bool".equals(arg.getDataType())) {
						arg.setUserValue(Boolean.valueOf(arg.getUserValue()).toString());
					} else {
						arg.setUserValue(arg.getDefaultValue());
					}
				} else {
					arg.setUserValue(null);
				}
				checkboxTreeViewer.refresh();
			}
		}
	}

	private final class ShowCheckedButtonSelectionListener extends SelectionAdapter {
		/** 显示所有参数还是仅仅被选中的参数 **/
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (((ToolItem) e.getSource()).getSelection()) {
				checkboxTreeViewer.addFilter(checkedFilter);
			} else {
				checkboxTreeViewer.removeFilter(checkedFilter);
			}
		}
	}

	private final class FilterDialogSelectionListener extends SelectionAdapter {
		/** 点击过滤按钮时，激活类型过滤窗口 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			typefilterDiaglog.open();
		}
	}

	private final class FilterTextInputModifyListener implements ModifyListener {
		/** 监听过滤文本框输入，内容不为空时候增加文本过滤器 */
		@Override
		public void modifyText(ModifyEvent arg0) {
			if (filterTextInput.getText().trim().length() > 0) {
				checkboxTreeViewer.addFilter(nameFilter);
			} else {
				checkboxTreeViewer.removeFilter(nameFilter);
			}
		}
	}

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private CheckboxTreeViewer checkboxTreeViewer;
	private Text filterTextInput;
	private Label commentLabel;
	private HotSpotArgumentsContentNameFilter nameFilter;
	private HotSpotArgumentsContentCheckedFilter checkedFilter = new HotSpotArgumentsContentCheckedFilter();
	private HotSpotArgumentsContentArgumentTypeFilter typeFilter = new HotSpotArgumentsContentArgumentTypeFilter();
	private HotSpotArgumentsContentProvider provider = new HotSpotArgumentsContentProvider();
	private ArgumentTypeFilterDetailsDialog typefilterDiaglog = new ArgumentTypeFilterDetailsDialog(getShell(), typeFilter, this);
	private ArgumentRepostory repostory;

	public CheckboxTreeViewer getCheckboxTreeViewer() {
		return checkboxTreeViewer;
	}

	/**
	 * 创建传输设置页面的控件，本方法由WindowBuilder Pro自动生成，避免人工编辑
	 */
	public HotSpotArgumentsComposite(Composite parent, int style, ArgumentRepostory repo) {
		super(parent, SWT.EMBEDDED);
		repostory = repo;

		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.setBorderStyle(SWT.NULL);
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(1, true));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 28;
		composite.setLayoutData(gd_composite);
		toolkit.adapt(composite);
		toolkit.paintBordersFor(composite);

		filterTextInput = new Text(composite, SWT.NONE);
		nameFilter = new HotSpotArgumentsContentNameFilter(filterTextInput);
		filterTextInput.addModifyListener(new FilterTextInputModifyListener());
		filterTextInput.setToolTipText("Filter");
		filterTextInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(filterTextInput, true, true);

		ToolBar toolBar = new ToolBar(composite, SWT.FLAT | SWT.RIGHT);
		toolkit.adapt(toolBar);
		toolkit.paintBordersFor(toolBar);

		ToolItem btnFilterDialog = new ToolItem(toolBar, SWT.NONE);
		btnFilterDialog.setToolTipText("Select argument types to display");
		btnFilterDialog.addSelectionListener(new FilterDialogSelectionListener());
		btnFilterDialog.setWidth(12);
		btnFilterDialog.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/datasheet.gif"));

		ToolItem btnShowChecked = new ToolItem(toolBar, SWT.CHECK);
		btnShowChecked.setToolTipText("Display checked arguments only");
		btnShowChecked.addSelectionListener(new ShowCheckedButtonSelectionListener());
		btnShowChecked.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/filter_history.gif"));
		
		ToolItem btnExpand = new ToolItem(toolBar, SWT.NONE);
		btnExpand.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				checkboxTreeViewer.expandAll();
			}
		});
		btnExpand.setToolTipText("Expand All");
		btnExpand.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/expandall.gif"));
		
		ToolItem btnCollapse = new ToolItem(toolBar, SWT.NONE);
		btnCollapse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				checkboxTreeViewer.collapseAll();
			}
		});
		btnCollapse.setToolTipText("Collapse all");
		btnCollapse.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/collapseall.gif"));

		checkboxTreeViewer = new CheckboxTreeViewer(this, SWT.CHECK | SWT.MULTI | SWT.FULL_SELECTION);
		checkboxTreeViewer.setUseHashlookup(true);

		checkboxTreeViewer.addCheckStateListener(new TreeViewerCheckStateListener());
		checkboxTreeViewer.setSorter(new HotSpotArgumnetsContentSorter());
		checkboxTreeViewer.setAutoExpandLevel(2);
		Tree tree = checkboxTreeViewer.getTree();
		tree.addSelectionListener(new TreeViewerSelectionListener());
		GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tree.widthHint = 640;
		gd_tree.heightHint = 375;
		tree.setLayoutData(gd_tree);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		toolkit.paintBordersFor(tree);

		TreeViewerColumn treeViewerColumn = new TreeViewerColumn(checkboxTreeViewer, SWT.NONE);
		TreeColumn trclArgumentName = treeViewerColumn.getColumn();
		trclArgumentName.setWidth(300);
		trclArgumentName.setText("Argument Name");

		TreeViewerColumn treeViewerColumn_5 = new TreeViewerColumn(checkboxTreeViewer, SWT.NONE);
		TreeColumn trclArgumentType = treeViewerColumn_5.getColumn();
		trclArgumentType.setWidth(80);
		trclArgumentType.setText("Category");

		TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(checkboxTreeViewer, SWT.NONE);
		TreeColumn trclDataType = treeViewerColumn_1.getColumn();
		trclDataType.setWidth(90);
		trclDataType.setText("Type");

		TreeViewerColumn treeViewerColumn_2 = new TreeViewerColumn(checkboxTreeViewer, SWT.NONE);
		TreeColumn trclDefaultValue = treeViewerColumn_2.getColumn();
		trclDefaultValue.setWidth(90);
		trclDefaultValue.setText("Default");

		TreeViewerColumn treeViewerColumn_3 = new TreeViewerColumn(checkboxTreeViewer, SWT.NONE);
		treeViewerColumn_3.setEditingSupport(new TreeViewerEditingSupporter(checkboxTreeViewer));
		TreeColumn trclUserValue = treeViewerColumn_3.getColumn();
		trclUserValue.setWidth(90);
		trclUserValue.setText("Value");

		// TreeViewerColumn treeViewerColumn_4 = new
		// TreeViewerColumn(checkboxTreeViewer, SWT.NONE);
		// TreeColumn trclComment = treeViewerColumn_4.getColumn();
		// trclComment.setWidth(200);
		// trclComment.setText("Comment");

		commentLabel = new Label(this, SWT.WRAP);
		GridData gd_commentLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_commentLabel.heightHint = 14;
		commentLabel.setLayoutData(gd_commentLabel);
		toolkit.adapt(commentLabel, true, true);
		checkboxTreeViewer.setLabelProvider(provider);
		checkboxTreeViewer.setContentProvider(provider);
		checkboxTreeViewer.setCheckStateProvider(provider);
		checkboxTreeViewer.addFilter(typeFilter);
		checkboxTreeViewer.setInput(repostory);
	}

	/**
	 * JUST FOR TEST
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				Display display = Display.getDefault();
				final Shell shell = new Shell();
				shell.setSize(650, 800);
				shell.setLayout(new FillLayout(SWT.HORIZONTAL));
				new HotSpotArgumentsComposite(shell, SWT.FULL_SELECTION, new ArgumentRepostory());

				shell.open();
				shell.layout();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
			}
		});
	}
}
