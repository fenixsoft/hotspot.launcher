package org.fenixsoft.hotspot.launch;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaApplicationLaunchShortcut;

/**
 * HotSpot Java Applicaton启动快捷方式
 * 
 * @author icyfenix@gmail.com
 */
public class HotSpotJavaApplicationLaunchShortcut extends JavaApplicationLaunchShortcut {

	public static final String TYPE_ID = "org.fenixsoft.hotspot.launch.localHotSpotJavaApplication";

	@Override
	protected ILaunchConfigurationType getConfigurationType() {
		return DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurationType(TYPE_ID);
	}

}
