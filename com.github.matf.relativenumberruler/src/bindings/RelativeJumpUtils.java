package bindings;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class RelativeJumpUtils {

	public static void jump(ExecutionEvent event, int offset) {
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		Control control = editor.getAdapter(Control.class);
		if (!(control instanceof StyledText)) {
			return;
		}
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
		ITextEditor textEditor;
		if (editorPart instanceof ITextEditor) {
			textEditor = (ITextEditor) editorPart;
		} else {
			textEditor = editorPart.getAdapter(ITextEditor.class);
			if (textEditor == null) {
				return;
			}
		}

		ISelectionProvider selectionProvider = textEditor.getSelectionProvider();
		ISelection selection = selectionProvider.getSelection();
		if (!(selection instanceof ITextSelection)) {
			return;
		}
		ITextSelection textSelection = (ITextSelection) selection;
		int currentLine = textSelection.getStartLine() + 1; // Convert 0-based to 1-based index
		int newLine = currentLine + offset;
		IDocumentProvider documentProvider = textEditor.getDocumentProvider();
		IDocument document = documentProvider.getDocument(textEditor.getEditorInput());
		try {
			int lineOffset = document.getLineOffset(newLine - 1); // convert back to 0-based
			textEditor.selectAndReveal(lineOffset, 0);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

}
