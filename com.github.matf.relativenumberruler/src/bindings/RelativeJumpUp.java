package bindings;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;

public class RelativeJumpUp extends AbstractHandler  {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		InputDialog dialog = new InputDialog(Display.getDefault().getActiveShell(), "Relative jump up",
				"Enter number of lines to jump up:", "0", null);
		dialog.open();
		int offset = Math.abs(Integer.parseInt(dialog.getValue())) * -1;
		RelativeJumpUtils.jump(event, offset);
		return null;
	}

}
