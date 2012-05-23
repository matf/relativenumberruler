package com.github.matf.relativenumberruler;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.JFaceTextUtil;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.internal.texteditor.PropertyEventDispatcher;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.rulers.IContributedRulerColumn;
import org.eclipse.ui.texteditor.rulers.RulerColumnDescriptor;

@SuppressWarnings("restriction")
public class RelativeLineNumberColumn extends LineNumberRulerColumn implements IContributedRulerColumn {
	private static final String FG_COLOR_KEY = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER_COLOR;
	private static final String BG_COLOR_KEY = AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND;
	private static final String USE_DEFAULT_BG_KEY = AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT;
	
	private ITextEditor editor;
	private RulerColumnDescriptor descriptor;
	private StyledText fCachedTextWidget;
	private ITextViewer fCachedTextViewer;

	@Override
	protected String createDisplayString(int line) {
		if (fCachedTextWidget == null || fCachedTextWidget.isDisposed()) return "";
		
		int currentLine = fCachedTextWidget.getLineAtOffset(fCachedTextWidget.getCaretOffset());
		int modelLine = JFaceTextUtil.widgetLine2ModelLine(fCachedTextViewer, currentLine);
		
		return Integer.toString(Math.abs(modelLine - line));
	}
	
	@Override
	public Control createControl(CompositeRuler parentRuler, Composite parentControl) {
		initialize();
		
		fCachedTextViewer = parentRuler.getTextViewer();
		fCachedTextWidget = fCachedTextViewer.getTextWidget();
		fCachedTextWidget.addCaretListener(new CaretListener() {
			public void caretMoved(CaretEvent event) {
				layout(true);
			}
		});
		
		return super.createControl(parentRuler, parentControl);
	}

	private void initialize() {
		// read color preferences
		final IPreferenceStore store = getPreferenceStore();
		updateForegroundColor(store);
		updateBackgroundColor(store);
		
		// listen to changes of color preferences, redraw if changed
		PropertyEventDispatcher fDispatcher = new PropertyEventDispatcher(store);

		fDispatcher.addPropertyChangeListener(FG_COLOR_KEY, new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				updateForegroundColor(store);
				redraw();
			}
		});
		IPropertyChangeListener backgroundHandler= new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				updateBackgroundColor(store);
				redraw();
			}
		};
		fDispatcher.addPropertyChangeListener(BG_COLOR_KEY, backgroundHandler);
		fDispatcher.addPropertyChangeListener(USE_DEFAULT_BG_KEY, backgroundHandler);
	}

	public RulerColumnDescriptor getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(RulerColumnDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	public void setEditor(ITextEditor editor) {
		this.editor = editor;
	}

	public ITextEditor getEditor() {
		return editor;
	}

	public void columnCreated() {
	}

	public void columnRemoved() {
	}

	protected ISharedTextColors getSharedColors() {
		return EditorsPlugin.getDefault().getSharedTextColors();
	}
	
	private IPreferenceStore getPreferenceStore() {
		return EditorsUI.getPreferenceStore();
	}
	
	private void updateForegroundColor(IPreferenceStore store) {
		RGB rgb=  getColorFromStore(store, FG_COLOR_KEY);
		if (rgb == null)
			rgb= new RGB(0, 0, 0);
		ISharedTextColors sharedColors= getSharedColors();
		setForeground(sharedColors.getColor(rgb));
	}

	private void updateBackgroundColor(IPreferenceStore store) {
		// background color: same as editor, or system default
		RGB rgb;
		if (store.getBoolean(USE_DEFAULT_BG_KEY))
			rgb= null;
		else
			rgb= getColorFromStore(store, BG_COLOR_KEY);
		ISharedTextColors sharedColors= getSharedColors();
		setBackground(sharedColors.getColor(rgb));
	}

	private static RGB getColorFromStore(IPreferenceStore store, String key) {
		RGB rgb= null;
		if (store.contains(key)) {
			if (store.isDefault(key))
				rgb= PreferenceConverter.getDefaultColor(store, key);
			else
				rgb= PreferenceConverter.getColor(store, key);
		}
		return rgb;
	}
}
