package cz.vutbr.fit.bms.xpalam00;

import java.io.IOException;
import javax.bluetooth.BluetoothStateException;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import org.netbeans.microedition.util.SimpleCancellableTask;

/**
 * Aplikace pro mobilní telefony vyhledávající Bluetooth zařízení.
 * Projekt 2 do BMS.
 *
 * @author Milan Pála, xpalam00
 */
public class VisualMIDlet extends MIDlet implements CommandListener {

    private boolean midletPaused = false;

    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private List list;
    private Command exitCommand;
    private Command exitCommand1;
    private Command backCommand;
    private Command itemCommand;
    private Command itemCommand1;
    private SimpleCancellableTask task;
    //</editor-fold>//GEN-END:|fields|0|

    /**
     * The VisualMIDlet constructor.
     */
    public VisualMIDlet() {
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
	* Initilizes the application.
	* It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
	*/
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
	* Performs an action assigned to the Mobile Device - MIDlet Started point.
	*/
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
	    switchDisplayable(null, getList());//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

	/**
	 * Smaže obrazovku a provede nové hledání. Aktivuje se levým soft. tlačítkem
	 */
	private void find()
	{
	    	list.deleteAll(); // smažu seznam
		
		LocalDevice localDevice;
		try {
			localDevice = LocalDevice.getLocalDevice();
			list.append("Moje: "+localDevice.getFriendlyName(), null);

			BluetoothScanner bluetoothscanner = new BluetoothScanner();

			// nalezneme ostatní zařízení
			DiscoveryAgent agent = localDevice.getDiscoveryAgent();
			agent.startInquiry(DiscoveryAgent.GIAC, bluetoothscanner);

			try {
				synchronized(bluetoothscanner.lock)
				{
					list.append("Hledám...", null);
					bluetoothscanner.lock.wait();
				}
			}
			catch (InterruptedException e) {
				list.append(e.getMessage(), null);
			}

			// vypíše nalezaná zařízení nebo chybu
			int pocetZarizeni = bluetoothscanner.vecDevices.size();
			if(pocetZarizeni <= 0)
			{
				list.delete(1); // smažu nápis hledám...
				list.append("Nic nenalezeno.", null);
			}
			else
			{
				list.delete(1); // smažu nápis hledám...

				for (int i = 0; i <pocetZarizeni; i++)
				{
					RemoteDevice remoteDevice=(RemoteDevice)bluetoothscanner.vecDevices.elementAt(i);
					list.append("#"+(i+1)+" "+remoteDevice.getFriendlyName(true), null);
				}
			}
		} catch (BluetoothStateException ex) {
			list.append("Vypnutý BT", null);
		}
		catch(IOException ex)
		{
			list.append(ex.getMessage(), null);
		}
	}

	//<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
	/**
	 * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
	 */
	public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
	}//GEN-BEGIN:|4-resumeMIDlet|2|
	//</editor-fold>//GEN-END:|4-resumeMIDlet|2|

	//<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
	/**
	 * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
	 * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
	 * @param nextDisplayable the Displayable to be set
	 */
	public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
		Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
		if (alert == null) {
			display.setCurrent(nextDisplayable);
		} else {
			display.setCurrent(alert, nextDisplayable);
		}//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
	}//GEN-BEGIN:|5-switchDisplayable|2|
	//</editor-fold>//GEN-END:|5-switchDisplayable|2|



	//<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
	/**
	 * Called by a system to indicated that a command has been invoked on a particular displayable.
	 * @param command the Command that was invoked
	 * @param displayable the Displayable where the command was invoked
	 */
	public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
		if (displayable == list) {//GEN-BEGIN:|7-commandAction|1|26-preAction
			if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|1|26-preAction
                // write pre-action user code here
				listAction();//GEN-LINE:|7-commandAction|2|26-postAction
                // write post-action user code here
			} else if (command == backCommand) {//GEN-LINE:|7-commandAction|3|36-preAction
                // write pre-action user code here
				exitMIDlet();//GEN-LINE:|7-commandAction|4|36-postAction
                // write post-action user code here
			} else if (command == itemCommand) {//GEN-LINE:|7-commandAction|5|39-preAction
                // write pre-action user code here
//GEN-LINE:|7-commandAction|6|39-postAction
                // write post-action user code here
			find();
			}//GEN-BEGIN:|7-commandAction|7|7-postCommandAction
		}//GEN-END:|7-commandAction|7|7-postCommandAction
        // write post-action user code here
	}//GEN-BEGIN:|7-commandAction|8|
	//</editor-fold>//GEN-END:|7-commandAction|8|




	//<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand ">//GEN-BEGIN:|21-getter|0|21-preInit
	/**
	 * Returns an initiliazed instance of exitCommand component.
	 * @return the initialized component instance
	 */
	public Command getExitCommand() {
		if (exitCommand == null) {//GEN-END:|21-getter|0|21-preInit
            // write pre-init user code here
			exitCommand = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|21-getter|1|21-postInit
            // write post-init user code here
		}//GEN-BEGIN:|21-getter|2|
		return exitCommand;
	}
	//</editor-fold>//GEN-END:|21-getter|2|



	//<editor-fold defaultstate="collapsed" desc=" Generated Getter: list ">//GEN-BEGIN:|25-getter|0|25-preInit
	/**
	 * Returns an initiliazed instance of list component.
	 * @return the initialized component instance
	 */
	public List getList() {
		if (list == null) {//GEN-END:|25-getter|0|25-preInit
            // write pre-init user code here
			list = new List("Seznam za\u0159\u00EDzen\u00ED", Choice.IMPLICIT);//GEN-BEGIN:|25-getter|1|25-postInit
			list.addCommand(getBackCommand());
			list.addCommand(getItemCommand());
			list.setCommandListener(this);
			list.setFitPolicy(Choice.TEXT_WRAP_DEFAULT);
			list.setSelectedFlags(new boolean[] {  });//GEN-END:|25-getter|1|25-postInit
            // write post-init user code here
		}//GEN-BEGIN:|25-getter|2|
		return list;
	}
	//</editor-fold>//GEN-END:|25-getter|2|

	//<editor-fold defaultstate="collapsed" desc=" Generated Method: listAction ">//GEN-BEGIN:|25-action|0|25-preAction
	/**
	 * Performs an action assigned to the selected list element in the list component.
	 */
	public void listAction() {//GEN-END:|25-action|0|25-preAction
        // enter pre-action user code here
		String __selectedString = getList().getString(getList().getSelectedIndex());//GEN-LINE:|25-action|1|25-postAction
        // enter post-action user code here
	}//GEN-BEGIN:|25-action|2|
	//</editor-fold>//GEN-END:|25-action|2|

	//<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand1 ">//GEN-BEGIN:|28-getter|0|28-preInit
	/**
	 * Returns an initiliazed instance of exitCommand1 component.
	 * @return the initialized component instance
	 */
	public Command getExitCommand1() {
		if (exitCommand1 == null) {//GEN-END:|28-getter|0|28-preInit
            // write pre-init user code here
			exitCommand1 = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|28-getter|1|28-postInit
            // write post-init user code here
		}//GEN-BEGIN:|28-getter|2|
		return exitCommand1;
	}
	//</editor-fold>//GEN-END:|28-getter|2|

	//<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand ">//GEN-BEGIN:|35-getter|0|35-preInit
	/**
	 * Returns an initiliazed instance of backCommand component.
	 * @return the initialized component instance
	 */
	public Command getBackCommand() {
		if (backCommand == null) {//GEN-END:|35-getter|0|35-preInit
            // write pre-init user code here
			backCommand = new Command("Vypnout", Command.EXIT, 0);//GEN-LINE:|35-getter|1|35-postInit
            // write post-init user code here
		}//GEN-BEGIN:|35-getter|2|
		return backCommand;
	}
	//</editor-fold>//GEN-END:|35-getter|2|

	//<editor-fold defaultstate="collapsed" desc=" Generated Getter: itemCommand ">//GEN-BEGIN:|38-getter|0|38-preInit
	/**
	 * Returns an initiliazed instance of itemCommand component.
	 * @return the initialized component instance
	 */
	public Command getItemCommand() {
		if (itemCommand == null) {//GEN-END:|38-getter|0|38-preInit
            // write pre-init user code here
			itemCommand = new Command("Naj\u00EDt", Command.ITEM, 0);//GEN-LINE:|38-getter|1|38-postInit
            // write post-init user code here
		}//GEN-BEGIN:|38-getter|2|
		return itemCommand;
	}
	//</editor-fold>//GEN-END:|38-getter|2|



	//<editor-fold defaultstate="collapsed" desc=" Generated Getter: task ">//GEN-BEGIN:|45-getter|0|45-preInit
	/**
	 * Returns an initiliazed instance of task component.
	 * @return the initialized component instance
	 */
	public SimpleCancellableTask getTask() {
		if (task == null) {//GEN-END:|45-getter|0|45-preInit
		    // write pre-init user code here
			task = new SimpleCancellableTask();//GEN-BEGIN:|45-getter|1|45-execute
			task.setExecutable(new org.netbeans.microedition.util.Executable() {
				public void execute() throws Exception {//GEN-END:|45-getter|1|45-execute
				    // write task-execution user code here
				}//GEN-BEGIN:|45-getter|2|45-postInit
			});//GEN-END:|45-getter|2|45-postInit
		    // write post-init user code here
		}//GEN-BEGIN:|45-getter|3|
		return task;
	}
	//</editor-fold>//GEN-END:|45-getter|3|

	//<editor-fold defaultstate="collapsed" desc=" Generated Getter: itemCommand1 ">//GEN-BEGIN:|46-getter|0|46-preInit
	/**
	 * Returns an initiliazed instance of itemCommand1 component.
	 * @return the initialized component instance
	 */
	public Command getItemCommand1() {
		if (itemCommand1 == null) {//GEN-END:|46-getter|0|46-preInit
		    // write pre-init user code here
			itemCommand1 = new Command("Item", Command.ITEM, 0);//GEN-LINE:|46-getter|1|46-postInit
		    // write post-init user code here
		}//GEN-BEGIN:|46-getter|2|
		return itemCommand1;
	}
	//</editor-fold>//GEN-END:|46-getter|2|

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay () {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable (null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet ();
        } else {
            initialize ();
            startMIDlet ();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }

}
