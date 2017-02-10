package cz.vutbr.fit.bms.xpalam00;

import java.util.Vector;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 * Třída pro nalezení bluetooth zařízení
 * Inspirováno podle: http://www.jsr82.com/jsr-82-sample-device-discovery/
 *
 * @author Milan Pála, xpalam00
 *
 */
public class BluetoothScanner implements DiscoveryListener
{
	public static Vector vecDevices = new Vector(); // seznam všech zařízení
	public static Object lock = new Object(); // zámek pro čekací dobu
	
	/**
	 * Provede pro každé nalezené zařízení
	 * @param btDevice
	 * @param cod
	 */
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod)
	{
		if(!vecDevices.contains(btDevice))
		{
			vecDevices.addElement(btDevice);
		}
	}
	//no need to implement this method since services are not being discovered
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
	}
	//no need to implement this method since services are not being discovered
	public void serviceSearchCompleted(int transID, int respCode) {
	}

	/**
	 * Zavolá po skončení hledání všech zařízení
	 */
	public void inquiryCompleted(int discType)
	{
		synchronized(lock)
		{
			lock.notify(); // uvolní zámek
		}
	}
}