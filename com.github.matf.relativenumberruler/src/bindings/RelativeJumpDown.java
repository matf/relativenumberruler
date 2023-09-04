package bindings;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;

public class RelativeJumpDown extends AbstractHandler  {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		InputDialog dialog = new InputDialog(Display.getDefault().getActiveShell(), "Relative jump down",
				"Enter number of lines to jump down:", "0", null);
		dialog.open();
		int offset = Math.abs(Integer.parseInt(dialog.getValue()));
		RelativeJumpUtils.jump(event, offset);
		return null;
	}

}
