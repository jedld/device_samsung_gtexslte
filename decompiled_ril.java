/*
* Decompiled with CFR 0_118.
*
* Could not load the following classes:
*  android.content.BroadcastReceiver
*  android.content.ContentResolver
*  android.content.Context
*  android.content.Intent
*  android.content.IntentFilter
*  android.hardware.display.DisplayManager
*  android.hardware.display.DisplayManager$DisplayListener
*  android.net.ConnectivityManager
*  android.net.LocalSocket
*  android.os.AsyncResult
*  android.os.Build
*  android.os.Bundle
*  android.os.Handler
*  android.os.HandlerThread
*  android.os.Looper
*  android.os.Message
*  android.os.Parcel
*  android.os.Parcelable
*  android.os.Parcelable$Creator
*  android.os.PowerManager
*  android.os.PowerManager$WakeLock
*  android.os.Registrant
*  android.os.RegistrantList
*  android.os.SystemProperties
*  android.preference.PreferenceManager
*  android.provider.Settings
*  android.provider.Settings$Global
*  android.provider.Settings$Secure
*  android.provider.Settings$System
*  android.telephony.CellInfo
*  android.telephony.NeighboringCellInfo
*  android.telephony.PhoneNumberUtils
*  android.telephony.Rlog
*  android.telephony.SignalStrength
*  android.telephony.SubscriptionManager
*  android.telephony.TelephonyManager
*  android.text.TextUtils
*  android.util.Base64
*  android.util.SparseArray
*  android.view.Display
*  com.android.internal.telephony.GsmAlphabet
*  com.sec.android.app.CscFeature
*/
package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.LocalSocket;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SignalStrength;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.CbConfig;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseArray;
import android.view.Display;
import com.android.internal.telephony.Am;
import com.android.internal.telephony.BaseCommands;
import com.android.internal.telephony.CallDetails;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallModify;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.DriverCall;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.HardwareConfig;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.PreferredNetworkListInfo;
import com.android.internal.telephony.RILRequest;
import com.android.internal.telephony.RadioCapability;
import com.android.internal.telephony.SSReleaseCompleteNotification;
import com.android.internal.telephony.SmsResponse;
import com.android.internal.telephony.TelephonyDevController;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.internal.telephony.cdma.CdmaInformationRecords;
import com.android.internal.telephony.cdma.CdmaSmsBroadcastConfigInfo;
import com.android.internal.telephony.dataconnection.DataCallResponse;
import com.android.internal.telephony.dataconnection.DataProfile;
import com.android.internal.telephony.dataconnection.DcFailCause;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SsData;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccIoResult;
import com.android.internal.telephony.uicc.IccRefreshResponse;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.SimLockInfoResult;
import com.android.internal.telephony.uicc.SimPBEntryResult;
import com.sec.android.app.CscFeature;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class RIL
extends BaseCommands
implements CommandsInterface {
   private static final int CDMA_BROADCAST_SMS_NO_OF_SERVICE_CATEGORIES = 31;
   private static final int CDMA_BSI_NO_OF_INTS_STRUCT = 3;
   public static final boolean CELL_BROADCAST_ENABLE = true;
   private static final int DEFAULT_WAKE_LOCK_TIMEOUT = 60000;
   static final String ETWS_TEST = "jp.co.android.softbankCBM.ETWS";
   static final int EVENT_SEND = 1;
   static final int EVENT_WAKE_LOCK_TIMEOUT = 2;
   static final String LOG_LEVEL_PROP = "ro.debug_level";
   static final String LOG_LEVEL_PROP_HIGH = "0x4948";
   static final String LOG_LEVEL_PROP_LOW = "0x4f4c";
   static final String LOG_LEVEL_PROP_MID = "0x494d";
   static final int NETTEXT_GSM_SMS_CBMI_LIST_SIZE_MAX = 100;
   private static final int OEM_FUNCTION_ID_NETWORK = 2;
   private static final int OEM_NET_GET_MODEM_CAP = 97;
   private static final int OEM_NET_UPDATE_SUB_BINDING = 98;
   static final int RESPONSE_SOLICITED = 0;
   static final int RESPONSE_UNSOLICITED = 1;
   static final boolean RILJ_LOGD = true;
   static final boolean RILJ_LOGV = false;
   static final String RILJ_LOG_TAG = "RILJ";
   static final int RIL_MAX_COMMAND_BYTES = 8192;
   static final boolean SHIP_BUILD = "true".equals(SystemProperties.get((String)"ro.product_ship", (String)"false"));
   static final String[] SOCKET_NAME_RIL = new String[]{"rild", "rild2", "rild3"};
   static final int SOCKET_OPEN_RETRY_MILLIS = 4000;
   static final int USSD_DCS_KS5601 = 148;
   static final String WAPPUSH_TEST = "jp.co.android.ETWS_WAPPUSH";
   private int initPhoneType = 0;
   Display mDefaultDisplay;
   int mDefaultDisplayState = 0;
   private final DisplayManager.DisplayListener mDisplayListener;
   private int mDtmfProgress;
   private Integer mInstanceId;
   BroadcastReceiver mIntentReceiver;
   Object mLastNITZTimeInfo;
   RILReceiver mReceiver;
   Thread mReceiverThread;
   SparseArray<RILRequest> mRequestList = new SparseArray();
   RILSender mSender;
   HandlerThread mSenderThread;
   LocalSocket mSocket;
   private Handler mSupportedRafHandler;
   AtomicBoolean mTestingEmergencyCall = new AtomicBoolean(false);
   PowerManager.WakeLock mWakeLock;
   int mWakeLockCount;
   final int mWakeLockTimeout;

   public RIL(Context context, int n, int n2) {
       this(context, n, n2, null);
   }

   /*
    * Enabled aggressive block sorting
    */
   public RIL(Context context, int n, int n2, Integer object) {
       super(context);
       this.mIntentReceiver = new BroadcastReceiver(){

           /*
            * Enabled aggressive block sorting
            */
           public void onReceive(Context object, Intent arrstring) {
               if (arrstring.getAction().equals("com.samsung.intent.action.EMERGENCY_STATE_CHANGED")) {
                   int n = arrstring.getIntExtra("reason", 0);
                   int n2 = arrstring.getIntExtra("enterType", 0);
                   if (n == 2 && n2 != 512) {
                       RIL.this.sendSafemode(true);
                       return;
                   }
                   if (n != 4) return;
                   if (n2 == 512) return;
                   RIL.this.sendSafemode(false);
                   return;
               }
               if (!arrstring.getAction().equals("jp.co.android.softbankCBM.ETWS") && !arrstring.getAction().equals("jp.co.android.ETWS_WAPPUSH")) {
                   Rlog.w((String)"RILJ", (String)("RIL received unexpected Intent: " + arrstring.getAction()));
                   return;
               }
               if (!"eng".equals(Build.TYPE)) return;
               if (!SystemProperties.getBoolean((String)"persist.EarthquakeTestmode", (boolean)false)) return;
               object = arrstring.getExtras();
               if (object == null) {
                   Rlog.d((String)"RILJ", (String)"intent.getExtras() = null");
                   return;
               }
               if (arrstring.getAction().equals("jp.co.android.softbankCBM.ETWS") && RIL.this.mGsmBroadcastSmsRegistrant != null) {
                   Rlog.d((String)"RILJ", (String)"ETWS_TEST receive");
                   object = object.get("pdus");
                   RIL.this.mGsmBroadcastSmsRegistrant.notifyRegistrant(new AsyncResult((Object)null, object, null));
                   return;
               }
               if (!arrstring.getAction().equals("jp.co.android.ETWS_WAPPUSH")) return;
               if (RIL.this.mGsmSmsRegistrant == null) return;
               Rlog.d((String)"RILJ", (String)"WAPPUSH_TEST receive");
               object = (String)object.get("pdus");
               arrstring = new String[2];
               arrstring[1] = object;
               Rlog.d((String)"RILJ", (String)"call newFromCMT");
               object = SmsMessage.newFromCMT(arrstring);
               Rlog.d((String)"RILJ", (String)"call mGsmSmsRegistrant notifyRegistrant");
               RIL.this.mGsmSmsRegistrant.notifyRegistrant(new AsyncResult((Object)null, object, null));
           }
       };
       this.mDisplayListener = new DisplayManager.DisplayListener(){

           public void onDisplayAdded(int n) {
           }

           public void onDisplayChanged(int n) {
               if (n == 0) {
                   RIL.this.updateScreenState();
               }
           }

           public void onDisplayRemoved(int n) {
           }
       };
       this.mSupportedRafHandler = new Handler(){

           /*
            * Enabled force condition propagation
            * Lifted jumps to return sites
            */
           public void handleMessage(Message object) {
               AsyncResult asyncResult = (AsyncResult)object.obj;
               object = (RadioCapability)asyncResult.result;
               if (asyncResult.exception != null) {
                   RIL.this.riljLog("Get supported radio access family fail");
                   do {
                       return;
                       break;
                   } while (true);
               }
               RIL.this.mSupportedRaf = object.getRadioAccessFamily();
               RIL.this.riljLog("Supported radio access family=" + RIL.this.mSupportedRaf);
           }
       };
       this.riljLog("RIL(context, preferredNetworkType=" + n + " cdmaSubscription=" + n2 + " instanceId=" + object + ")");
       this.mContext = context;
       this.mCdmaSubscription = n2;
       this.mPreferredNetworkType = n;
       this.mDtmfProgress = 0;
       this.mPhoneType = 0;
       this.mInstanceId = object;
       PowerManager powerManager = (PowerManager)context.getSystemService("power");
       StringBuilder stringBuilder = new StringBuilder().append("RILJ");
       object = this.mInstanceId == null ? "" : this.mInstanceId;
       this.mWakeLock = powerManager.newWakeLock(1, stringBuilder.append(object).toString());
       this.mWakeLock.setReferenceCounted(false);
       this.mWakeLockTimeout = SystemProperties.getInt((String)"ro.ril.wake_lock_timeout", (int)60000);
       this.mWakeLockCount = 0;
       this.mSenderThread = new HandlerThread("RILSender" + this.mInstanceId);
       this.mSenderThread.start();
       this.mSender = new RILSender(this.mSenderThread.getLooper());
       if (!((ConnectivityManager)context.getSystemService("connectivity")).isNetworkSupported(0)) {
           this.riljLog("Not starting RILReceiver: wifi-only");
       } else {
           this.riljLog("Starting RILReceiver" + this.mInstanceId);
           this.mReceiver = new RILReceiver();
           this.mReceiverThread = new Thread((Runnable)this.mReceiver, "RILReceiver" + this.mInstanceId);
           this.mReceiverThread.start();
           object = new IntentFilter();
           object.addAction("jp.co.android.softbankCBM.ETWS");
           object.addAction("jp.co.android.ETWS_WAPPUSH");
           context.registerReceiver(this.mIntentReceiver, (IntentFilter)object);
           context = (DisplayManager)context.getSystemService("display");
           this.mDefaultDisplay = context.getDisplay(0);
           context.registerDisplayListener(this.mDisplayListener, null);
       }
       TelephonyDevController.getInstance();
       TelephonyDevController.registerRIL(this);
   }

   static /* synthetic */ void access$200(RIL rIL) {
       rIL.decrementWakeLock();
   }

   static /* synthetic */ RILRequest access$300(RIL rIL, int n) {
       return rIL.findAndRemoveRequestFromList(n);
   }

   static /* synthetic */ boolean access$400(RIL rIL) {
       return rIL.clearWakeLock();
   }

   static /* synthetic */ Integer access$500(RIL rIL) {
       return rIL.mInstanceId;
   }

   static /* synthetic */ int access$600(InputStream inputStream, byte[] arrby) throws IOException {
       return RIL.readRilMessage(inputStream, arrby);
   }

   static /* synthetic */ void access$700(RIL rIL, Parcel parcel) {
       rIL.processResponse(parcel);
   }

   static /* synthetic */ void access$800(RIL rIL, int n, boolean bl) {
       rIL.clearRequestList(n, bl);
   }

   static /* synthetic */ void access$900(RIL rIL, int n) {
       rIL.notifyRegistrantsRilConnectionChanged(n);
   }

   /*
    * Enabled aggressive block sorting
    * Enabled unnecessary exception pruning
    * Enabled aggressive exception aggregation
    */
   private void acquireWakeLock() {
       PowerManager.WakeLock wakeLock = this.mWakeLock;
       synchronized (wakeLock) {
           this.mWakeLock.acquire();
           ++this.mWakeLockCount;
           this.mSender.removeMessages(2);
           Message message = this.mSender.obtainMessage(2);
           this.mSender.sendMessageDelayed(message, (long)this.mWakeLockTimeout);
           return;
       }
   }

   /*
    * Enabled aggressive block sorting
    * Enabled unnecessary exception pruning
    * Enabled aggressive exception aggregation
    */
   private void clearRequestList(int n, boolean bl) {
       SparseArray<RILRequest> sparseArray = this.mRequestList;
       synchronized (sparseArray) {
           Object object;
           int n2 = this.mRequestList.size();
           if (bl) {
               object = new Object();
               Rlog.d((String)"RILJ", (String)object.append("clearRequestList  mWakeLockCount=").append(this.mWakeLockCount).append(" mRequestList=").append(n2).toString());
           }
           int n3 = 0;
           do {
               if (n3 >= n2) {
                   this.mRequestList.clear();
                   return;
               }
               object = (RILRequest)this.mRequestList.valueAt(n3);
               if (bl) {
                   StringBuilder stringBuilder = new StringBuilder();
                   Rlog.d((String)"RILJ", (String)stringBuilder.append(n3).append(": [").append(object.mSerial).append("] ").append(RIL.requestToString(object.mRequest)).toString());
               }
               object.onError(n, null);
               object.release();
               this.decrementWakeLock();
               ++n3;
           } while (true);
       }
   }

   /*
    * Enabled aggressive block sorting
    * Enabled unnecessary exception pruning
    * Enabled aggressive exception aggregation
    */
   private boolean clearWakeLock() {
       boolean bl = false;
       PowerManager.WakeLock wakeLock = this.mWakeLock;
       synchronized (wakeLock) {
           if (this.mWakeLockCount == 0 && !this.mWakeLock.isHeld()) {
               return bl;
           }
           StringBuilder stringBuilder = new StringBuilder();
           Rlog.d((String)"RILJ", (String)stringBuilder.append("NOTE: mWakeLockCount is ").append(this.mWakeLockCount).append("at time of clearing").toString());
           this.mWakeLockCount = 0;
           this.mWakeLock.release();
           this.mSender.removeMessages(2);
           return true;
       }
   }

   private void constructCdmaSendSmsRilRequest(RILRequest rILRequest, byte[] object) {
       int n;
       object = new DataInputStream(new ByteArrayInputStream((byte[])object));
       rILRequest.mParcel.writeInt(object.readInt());
       rILRequest.mParcel.writeByte((byte)object.readInt());
       rILRequest.mParcel.writeInt(object.readInt());
       rILRequest.mParcel.writeInt(object.read());
       rILRequest.mParcel.writeInt(object.read());
       rILRequest.mParcel.writeInt(object.read());
       rILRequest.mParcel.writeInt(object.read());
       int n2 = object.read();
       rILRequest.mParcel.writeByte((byte)n2);
       for (n = 0; n < n2; ++n) {
           rILRequest.mParcel.writeByte(object.readByte());
           continue;
       }
       rILRequest.mParcel.writeInt(object.read());
       rILRequest.mParcel.writeByte((byte)object.read());
       n2 = (byte)object.read();
       rILRequest.mParcel.writeByte((byte)n2);
       for (n = 0; n < n2; ++n) {
           rILRequest.mParcel.writeByte(object.readByte());
           continue;
       }
       n2 = object.read();
       rILRequest.mParcel.writeInt(n2);
       for (n = 0; n < n2; ++n) {
           try {
               rILRequest.mParcel.writeByte(object.readByte());
               continue;
           }
           catch (IOException var1_2) {
               this.riljLog("sendSmsCdma: conversion from input stream to object failed: " + var1_2);
               break;
           }
       }
   }

   private void constructGsmSendSmsRilRequest(RILRequest rILRequest, String string, String string2) {
       rILRequest.mParcel.writeInt(2);
       rILRequest.mParcel.writeString(string);
       rILRequest.mParcel.writeString(string2);
   }

   /*
    * Enabled aggressive block sorting
    * Enabled unnecessary exception pruning
    * Enabled aggressive exception aggregation
    */
   private void decrementWakeLock() {
       PowerManager.WakeLock wakeLock = this.mWakeLock;
       synchronized (wakeLock) {
           if (this.mWakeLockCount > 1) {
               --this.mWakeLockCount;
           } else {
               this.mWakeLockCount = 0;
               this.mWakeLock.release();
               this.mSender.removeMessages(2);
           }
           return;
       }
   }

   /*
    * Enabled aggressive block sorting
    * Enabled unnecessary exception pruning
    * Enabled aggressive exception aggregation
    */
   private RILRequest findAndRemoveRequestFromList(int n) {
       SparseArray<RILRequest> sparseArray = this.mRequestList;
       synchronized (sparseArray) {
           RILRequest rILRequest = (RILRequest)this.mRequestList.get(n);
           if (rILRequest != null) {
               this.mRequestList.remove(n);
           }
           return rILRequest;
       }
   }

   /*
    * Enabled aggressive block sorting
    */
   private DataCallResponse getDataCallResponse(Parcel object, int n) {
       DataCallResponse dataCallResponse = new DataCallResponse();
       dataCallResponse.version = n;
       if (n < 5) {
           dataCallResponse.cid = object.readInt();
           dataCallResponse.active = object.readInt();
           dataCallResponse.type = object.readString();
           if (TextUtils.isEmpty((CharSequence)(object = object.readString()))) return dataCallResponse;
           dataCallResponse.addresses = object.split(" ");
           return dataCallResponse;
       }
       dataCallResponse.status = object.readInt();
       dataCallResponse.suggestedRetryTime = object.readInt();
       dataCallResponse.cid = object.readInt();
       dataCallResponse.active = object.readInt();
       dataCallResponse.type = object.readString();
       dataCallResponse.ifname = object.readString();
       if (dataCallResponse.status == DcFailCause.NONE.getErrorCode() && TextUtils.isEmpty((CharSequence)dataCallResponse.ifname)) {
           throw new RuntimeException("getDataCallResponse, no ifname");
       }
       String string = object.readString();
       if (!TextUtils.isEmpty((CharSequence)string)) {
           dataCallResponse.addresses = string.split(" ");
       }
       if (!TextUtils.isEmpty((CharSequence)(string = object.readString()))) {
           dataCallResponse.dnses = string.split(" ");
       }
       if (!TextUtils.isEmpty((CharSequence)(string = object.readString()))) {
           dataCallResponse.gateways = string.split(" ");
       }
       if (n >= 10 && !TextUtils.isEmpty((CharSequence)(string = object.readString()))) {
           dataCallResponse.pcscf = string.split(" ");
       }
       if (n < 11) return dataCallResponse;
       dataCallResponse.mtu = object.readInt();
       return dataCallResponse;
   }

   /*
    * Enabled force condition propagation
    * Lifted jumps to return sites
    */
   private CommandsInterface.RadioState getRadioStateFromInt(int n) {
       switch (n) {
           default: {
               throw new RuntimeException("Unrecognized RIL_RadioState: " + n);
           }
           case 0: {
               return CommandsInterface.RadioState.RADIO_OFF;
           }
           case 1: {
               return CommandsInterface.RadioState.RADIO_UNAVAILABLE;
           }
           case 10:
       }
       return CommandsInterface.RadioState.RADIO_ON;
   }

   private void iccTransmitApduHelper(int n, int n2, int n3, int n4, int n5, int n6, int n7, String string, Message object) {
       object = RILRequest.obtain(n, (Message)object);
       object.mParcel.writeInt(n2);
       object.mParcel.writeInt(n3);
       object.mParcel.writeInt(n4);
       object.mParcel.writeInt(n5);
       object.mParcel.writeInt(n6);
       object.mParcel.writeInt(n7);
       object.mParcel.writeString(string);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   /*
    * Enabled force condition propagation
    * Lifted jumps to return sites
    */
   private boolean isDebugLevelNotLow() {
       if (!SystemProperties.get((String)"ro.debug_level", (String)"0x4f4c").equalsIgnoreCase("0x4f4c")) return true;
       return false;
   }

   /*
    * Enabled aggressive block sorting
    */
   private void notifyRegistrantsCdmaInfoRec(CdmaInformationRecords cdmaInformationRecords) {
       if (cdmaInformationRecords.record instanceof CdmaInformationRecords.CdmaDisplayInfoRec) {
           if (this.mDisplayInfoRegistrants == null) return;
           this.unsljLogRet(1027, cdmaInformationRecords.record);
           this.mDisplayInfoRegistrants.notifyRegistrants(new AsyncResult((Object)null, cdmaInformationRecords.record, null));
           return;
       }
       if (cdmaInformationRecords.record instanceof CdmaInformationRecords.CdmaSignalInfoRec) {
           if (this.mSignalInfoRegistrants == null) return;
           this.unsljLogRet(1027, cdmaInformationRecords.record);
           this.mSignalInfoRegistrants.notifyRegistrants(new AsyncResult((Object)null, cdmaInformationRecords.record, null));
           return;
       }
       if (cdmaInformationRecords.record instanceof CdmaInformationRecords.CdmaNumberInfoRec) {
           if (this.mNumberInfoRegistrants == null) return;
           this.unsljLogRet(1027, cdmaInformationRecords.record);
           this.mNumberInfoRegistrants.notifyRegistrants(new AsyncResult((Object)null, cdmaInformationRecords.record, null));
           return;
       }
       if (cdmaInformationRecords.record instanceof CdmaInformationRecords.CdmaRedirectingNumberInfoRec) {
           if (this.mRedirNumInfoRegistrants == null) return;
           this.unsljLogRet(1027, cdmaInformationRecords.record);
           this.mRedirNumInfoRegistrants.notifyRegistrants(new AsyncResult((Object)null, cdmaInformationRecords.record, null));
           return;
       }
       if (cdmaInformationRecords.record instanceof CdmaInformationRecords.CdmaLineControlInfoRec) {
           if (this.mLineControlInfoRegistrants == null) return;
           this.unsljLogRet(1027, cdmaInformationRecords.record);
           this.mLineControlInfoRegistrants.notifyRegistrants(new AsyncResult((Object)null, cdmaInformationRecords.record, null));
           return;
       }
       if (cdmaInformationRecords.record instanceof CdmaInformationRecords.CdmaT53ClirInfoRec) {
           if (this.mT53ClirInfoRegistrants == null) return;
           this.unsljLogRet(1027, cdmaInformationRecords.record);
           this.mT53ClirInfoRegistrants.notifyRegistrants(new AsyncResult((Object)null, cdmaInformationRecords.record, null));
           return;
       }
       if (!(cdmaInformationRecords.record instanceof CdmaInformationRecords.CdmaT53AudioControlInfoRec)) return;
       if (this.mT53AudCntrlInfoRegistrants == null) return;
       this.unsljLogRet(1027, cdmaInformationRecords.record);
       this.mT53AudCntrlInfoRegistrants.notifyRegistrants(new AsyncResult((Object)null, cdmaInformationRecords.record, null));
   }

   private void notifyRegistrantsRilConnectionChanged(int n) {
       this.mRilVersion = n;
       if (this.mRilConnectedRegistrants != null) {
           this.mRilConnectedRegistrants.notifyRegistrants(new AsyncResult((Object)null, (Object)new Integer(n), null));
       }
   }

   /*
    * Enabled aggressive block sorting
    */
   private void processResponse(Parcel object) {
       int n = object.readInt();
       if (n == 1) {
           this.processUnsolicited((Parcel)object);
           return;
       }
       if (n != 0) return;
       if ((object = this.processSolicited((Parcel)object)) == null) return;
       object.release();
       this.decrementWakeLock();
   }

   /*
    * Enabled aggressive block sorting
    */
   private static int readRilMessage(InputStream inputStream, byte[] arrby) throws IOException {
       int n;
       int n2;
       int n3 = 0;
       int n4 = 4;
       do {
           if ((n = inputStream.read(arrby, n3, n4)) < 0) {
               Rlog.e((String)"RILJ", (String)"Hit EOS reading message length");
               return -1;
           }
           n3 += n;
           n4 = n = n4 - n;
       } while (n > 0);
       n4 = (arrby[0] & 255) << 24 | (arrby[1] & 255) << 16 | (arrby[2] & 255) << 8 | arrby[3] & 255;
       n = 0;
       n3 = n4;
       do {
           if ((n2 = inputStream.read(arrby, n, n3)) < 0) {
               Rlog.e((String)"RILJ", (String)("Hit EOS reading message.  messageLength=" + n4 + " remaining=" + n3));
               return -1;
           }
           n += n2;
           n3 = n2 = n3 - n2;
       } while (n2 > 0);
       return n4;
   }

   /*
    * Enabled force condition propagation
    * Lifted jumps to return sites
    */
   static String requestToString(int n) {
       switch (n) {
           default: {
               return "<unknown request>";
           }
           case 1: {
               return "GET_SIM_STATUS";
           }
           case 2: {
               return "ENTER_SIM_PIN";
           }
           case 3: {
               return "ENTER_SIM_PUK";
           }
           case 4: {
               return "ENTER_SIM_PIN2";
           }
           case 5: {
               return "ENTER_SIM_PUK2";
           }
           case 6: {
               return "CHANGE_SIM_PIN";
           }
           case 7: {
               return "CHANGE_SIM_PIN2";
           }
           case 8: {
               return "ENTER_NETWORK_DEPERSONALIZATION";
           }
           case 9: {
               return "GET_CURRENT_CALLS";
           }
           case 10: {
               return "DIAL";
           }
           case 11: {
               return "GET_IMSI";
           }
           case 12: {
               return "HANGUP";
           }
           case 13: {
               return "HANGUP_WAITING_OR_BACKGROUND";
           }
           case 14: {
               return "HANGUP_FOREGROUND_RESUME_BACKGROUND";
           }
           case 15: {
               return "REQUEST_SWITCH_WAITING_OR_HOLDING_AND_ACTIVE";
           }
           case 16: {
               return "CONFERENCE";
           }
           case 17: {
               return "UDUB";
           }
           case 18: {
               return "LAST_CALL_FAIL_CAUSE";
           }
           case 19: {
               return "SIGNAL_STRENGTH";
           }
           case 20: {
               return "VOICE_REGISTRATION_STATE";
           }
           case 21: {
               return "DATA_REGISTRATION_STATE";
           }
           case 22: {
               return "OPERATOR";
           }
           case 23: {
               return "RADIO_POWER";
           }
           case 24: {
               return "DTMF";
           }
           case 25: {
               return "SEND_SMS";
           }
           case 26: {
               return "SEND_SMS_EXPECT_MORE";
           }
           case 27: {
               return "SETUP_DATA_CALL";
           }
           case 28: {
               return "SIM_IO";
           }
           case 29: {
               return "SEND_USSD";
           }
           case 30: {
               return "CANCEL_USSD";
           }
           case 31: {
               return "GET_CLIR";
           }
           case 32: {
               return "SET_CLIR";
           }
           case 33: {
               return "QUERY_CALL_FORWARD_STATUS";
           }
           case 34: {
               return "SET_CALL_FORWARD";
           }
           case 35: {
               return "QUERY_CALL_WAITING";
           }
           case 36: {
               return "SET_CALL_WAITING";
           }
           case 37: {
               return "SMS_ACKNOWLEDGE";
           }
           case 38: {
               return "GET_IMEI";
           }
           case 39: {
               return "GET_IMEISV";
           }
           case 40: {
               return "ANSWER";
           }
           case 41: {
               return "DEACTIVATE_DATA_CALL";
           }
           case 42: {
               return "QUERY_FACILITY_LOCK";
           }
           case 43: {
               return "SET_FACILITY_LOCK";
           }
           case 44: {
               return "CHANGE_BARRING_PASSWORD";
           }
           case 45: {
               return "QUERY_NETWORK_SELECTION_MODE";
           }
           case 46: {
               return "SET_NETWORK_SELECTION_AUTOMATIC";
           }
           case 47: {
               return "SET_NETWORK_SELECTION_MANUAL";
           }
           case 48: {
               return "QUERY_AVAILABLE_NETWORKS ";
           }
           case 49: {
               return "DTMF_START";
           }
           case 50: {
               return "DTMF_STOP";
           }
           case 51: {
               return "BASEBAND_VERSION";
           }
           case 52: {
               return "SEPARATE_CONNECTION";
           }
           case 53: {
               return "SET_MUTE";
           }
           case 54: {
               return "GET_MUTE";
           }
           case 55: {
               return "QUERY_CLIP";
           }
           case 10029: {
               return "QUERY_CNAP";
           }
           case 56: {
               return "LAST_DATA_CALL_FAIL_CAUSE";
           }
           case 57: {
               return "DATA_CALL_LIST";
           }
           case 58: {
               return "RESET_RADIO";
           }
           case 59: {
               return "OEM_HOOK_RAW";
           }
           case 60: {
               return "OEM_HOOK_STRINGS";
           }
           case 61: {
               return "SCREEN_STATE";
           }
           case 62: {
               return "SET_SUPP_SVC_NOTIFICATION";
           }
           case 63: {
               return "WRITE_SMS_TO_SIM";
           }
           case 64: {
               return "DELETE_SMS_ON_SIM";
           }
           case 65: {
               return "SET_BAND_MODE";
           }
           case 66: {
               return "QUERY_AVAILABLE_BAND_MODE";
           }
           case 67: {
               return "REQUEST_STK_GET_PROFILE";
           }
           case 68: {
               return "REQUEST_STK_SET_PROFILE";
           }
           case 69: {
               return "REQUEST_STK_SEND_ENVELOPE_COMMAND";
           }
           case 70: {
               return "REQUEST_STK_SEND_TERMINAL_RESPONSE";
           }
           case 71: {
               return "REQUEST_STK_HANDLE_CALL_SETUP_REQUESTED_FROM_SIM";
           }
           case 72: {
               return "REQUEST_EXPLICIT_CALL_TRANSFER";
           }
           case 73: {
               return "REQUEST_SET_PREFERRED_NETWORK_TYPE";
           }
           case 74: {
               return "REQUEST_GET_PREFERRED_NETWORK_TYPE";
           }
           case 75: {
               return "REQUEST_GET_NEIGHBORING_CELL_IDS";
           }
           case 76: {
               return "REQUEST_SET_LOCATION_UPDATES";
           }
           case 77: {
               return "RIL_REQUEST_CDMA_SET_SUBSCRIPTION_SOURCE";
           }
           case 78: {
               return "RIL_REQUEST_CDMA_SET_ROAMING_PREFERENCE";
           }
           case 79: {
               return "RIL_REQUEST_CDMA_QUERY_ROAMING_PREFERENCE";
           }
           case 80: {
               return "RIL_REQUEST_SET_TTY_MODE";
           }
           case 81: {
               return "RIL_REQUEST_QUERY_TTY_MODE";
           }
           case 82: {
               return "RIL_REQUEST_CDMA_SET_PREFERRED_VOICE_PRIVACY_MODE";
           }
           case 83: {
               return "RIL_REQUEST_CDMA_QUERY_PREFERRED_VOICE_PRIVACY_MODE";
           }
           case 84: {
               return "RIL_REQUEST_CDMA_FLASH";
           }
           case 85: {
               return "RIL_REQUEST_CDMA_BURST_DTMF";
           }
           case 87: {
               return "RIL_REQUEST_CDMA_SEND_SMS";
           }
           case 88: {
               return "RIL_REQUEST_CDMA_SMS_ACKNOWLEDGE";
           }
           case 89: {
               return "RIL_REQUEST_GSM_GET_BROADCAST_CONFIG";
           }
           case 90: {
               return "RIL_REQUEST_GSM_SET_BROADCAST_CONFIG";
           }
           case 92: {
               return "RIL_REQUEST_CDMA_GET_BROADCAST_CONFIG";
           }
           case 93: {
               return "RIL_REQUEST_CDMA_SET_BROADCAST_CONFIG";
           }
           case 91: {
               return "RIL_REQUEST_GSM_BROADCAST_ACTIVATION";
           }
           case 86: {
               return "RIL_REQUEST_CDMA_VALIDATE_AND_WRITE_AKEY";
           }
           case 94: {
               return "RIL_REQUEST_CDMA_BROADCAST_ACTIVATION";
           }
           case 95: {
               return "RIL_REQUEST_CDMA_SUBSCRIPTION";
           }
           case 96: {
               return "RIL_REQUEST_CDMA_WRITE_SMS_TO_RUIM";
           }
           case 97: {
               return "RIL_REQUEST_CDMA_DELETE_SMS_ON_RUIM";
           }
           case 98: {
               return "RIL_REQUEST_DEVICE_IDENTITY";
           }
           case 100: {
               return "RIL_REQUEST_GET_SMSC_ADDRESS";
           }
           case 101: {
               return "RIL_REQUEST_SET_SMSC_ADDRESS";
           }
           case 99: {
               return "REQUEST_EXIT_EMERGENCY_CALLBACK_MODE";
           }
           case 102: {
               return "RIL_REQUEST_REPORT_SMS_MEMORY_STATUS";
           }
           case 103: {
               return "RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING";
           }
           case 104: {
               return "RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE";
           }
           case 105: {
               return "RIL_REQUEST_ISIM_AUTHENTICATION";
           }
           case 106: {
               return "RIL_REQUEST_ACKNOWLEDGE_INCOMING_GSM_SMS_WITH_PDU";
           }
           case 107: {
               return "RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS";
           }
           case 108: {
               return "RIL_REQUEST_VOICE_RADIO_TECH";
           }
           case 109: {
               return "RIL_REQUEST_GET_CELL_INFO_LIST";
           }
           case 110: {
               return "RIL_REQUEST_SET_CELL_INFO_LIST_RATE";
           }
           case 111: {
               return "RIL_REQUEST_SET_INITIAL_ATTACH_APN";
           }
           case 128: {
               return "RIL_REQUEST_SET_DATA_PROFILE";
           }
           case 112: {
               return "RIL_REQUEST_IMS_REGISTRATION_STATE";
           }
           case 113: {
               return "RIL_REQUEST_IMS_SEND_SMS";
           }
           case 114: {
               return "RIL_REQUEST_SIM_TRANSMIT_APDU_BASIC";
           }
           case 115: {
               return "RIL_REQUEST_SIM_OPEN_CHANNEL";
           }
           case 116: {
               return "RIL_REQUEST_SIM_CLOSE_CHANNEL";
           }
           case 117: {
               return "RIL_REQUEST_SIM_TRANSMIT_APDU_CHANNEL";
           }
           case 118: {
               return "RIL_REQUEST_NV_READ_ITEM";
           }
           case 119: {
               return "RIL_REQUEST_NV_WRITE_ITEM";
           }
           case 120: {
               return "RIL_REQUEST_NV_WRITE_CDMA_PRL";
           }
           case 121: {
               return "RIL_REQUEST_NV_RESET_CONFIG";
           }
           case 122: {
               return "RIL_REQUEST_SET_UICC_SUBSCRIPTION";
           }
           case 123: {
               return "RIL_REQUEST_ALLOW_DATA";
           }
           case 124: {
               return "GET_HARDWARE_CONFIG";
           }
           case 125: {
               return "RIL_REQUEST_SIM_AUTHENTICATION";
           }
           case 129: {
               return "RIL_REQUEST_SHUTDOWN";
           }
           case 131: {
               return "RIL_REQUEST_SET_RADIO_CAPABILITY";
           }
           case 130: {
               return "RIL_REQUEST_GET_RADIO_CAPABILITY";
           }
           case 10001: {
               return "DIAL_EMERGENCY_CALL";
           }
           case 10002: {
               return "CALL_DEFLECTION";
           }
           case 10003: {
               return "MODIFY_CALL_INITIATE";
           }
           case 10004: {
               return "MODIFY_CALL_CONFIRM";
           }
           case 10005: {
               return "SET_VOICE_DOMAIN_PREF";
           }
           case 10006: {
               return "SAFE_MODE";
           }
           case 10007: {
               return "SET_TRANSMIT_POWER";
           }
           case 10008: {
               return "GET_CELL_BROADCAST_CONFIG";
           }
           case 10009: {
               return "GET_PHONEBOOK_STORAGE_INFO";
           }
           case 10010: {
               return "GET_PHONEBOOK_ENTRY";
           }
           case 10011: {
               return "ACCESS_PHONEBOOK_ENTRY";
           }
           case 10012: {
               return "USIM_PB_CAPA";
           }
           case 10013: {
               return "LOCK_INFO";
           }
           case 10014: {
               return "STK_SIM_INIT_EVENT";
           }
           case 10015: {
               return "SET_PREFERRED_NETWORK_LIST";
           }
           case 10016: {
               return "GET_PREFERRED_NETWORK_LIST";
           }
           case 10017: {
               return "CHANGE_SIM_PERSO";
           }
           case 10018: {
               return "ENTER_SIM_PERSO";
           }
           case 10019: {
               return "SEND_ENCODED_USSD";
           }
           case 10020: {
               return "CDMA_SEND_SMS_EXPECT_MORE";
           }
           case 10021: {
               return "HANGUP_VT";
           }
           case 10022: {
               return "REQUEST_HOLD";
           }
           case 10023: {
               return "SET_SIM_POWER";
           }
           case 10024: {
               return "SET_LTE_BAND_MODE";
           }
           case 10025: {
               return "UICC_GBA_AUTHENTICATE_BOOTSTRAP";
           }
           case 10026: {
               return "UICC_GBA_AUTHENTICATE_NAF";
           }
           case 10027: {
               return "GET_INCOMING_COMMUNICATION_BARRING";
           }
           case 10028:
       }
       return "SET_INCOMING_COMMUNICATION_BARRING";
   }

   private Object responseBootstrap(Parcel parcel) {
       Bundle bundle = new Bundle();
       bundle.putByteArray("res", IccUtils.hexStringToBytes(parcel.readString()));
       bundle.putByteArray("auts", IccUtils.hexStringToBytes(parcel.readString()));
       return bundle;
   }

   private Object responseCallForward(Parcel parcel) {
       int n = parcel.readInt();
       CallForwardInfo[] arrcallForwardInfo = new CallForwardInfo[n];
       for (int i = 0; i < n; ++i) {
           arrcallForwardInfo[i] = new CallForwardInfo();
           arrcallForwardInfo[i].status = parcel.readInt();
           arrcallForwardInfo[i].reason = parcel.readInt();
           arrcallForwardInfo[i].serviceClass = parcel.readInt();
           arrcallForwardInfo[i].toa = parcel.readInt();
           arrcallForwardInfo[i].number = parcel.readString();
           arrcallForwardInfo[i].timeSeconds = parcel.readInt();
           String string = parcel.readString();
           CallForwardInfo callForwardInfo = arrcallForwardInfo[i];
           String string2 = string;
           if (string == null) {
               string2 = "";
           }
           callForwardInfo.startTime = string2;
           string = parcel.readString();
           callForwardInfo = arrcallForwardInfo[i];
           string2 = string;
           if (string == null) {
               string2 = "";
           }
           callForwardInfo.endTime = string2;
       }
       return arrcallForwardInfo;
   }

   private Object responseCallModify(Parcel object) {
       CallModify callModify = new CallModify();
       callModify.call_index = object.readInt();
       int n = object.readInt();
       int n2 = object.readInt();
       object = object.readString();
       callModify.call_details = new CallDetails(n, n2, null);
       callModify.call_details.setExtrasFromCsv((String)object);
       return callModify;
   }

   private Object responseCallRing(Parcel parcel) {
       return new char[]{(char)parcel.readInt(), (char)parcel.readInt(), (char)parcel.readInt(), (char)parcel.readInt()};
   }

   /*
    * Enabled aggressive block sorting
    */
   private Object responseCbSettings(Parcel arrby) {
       int n = 0;
       CbConfig cbConfig = new CbConfig();
       Rlog.d((String)"RILJ", (String)"responseCbSettings");
       int n2 = arrby.readInt();
       if (n2 == 1) {
           cbConfig.bCBEnabled = true;
       } else if (n2 == 2) {
           cbConfig.bCBEnabled = false;
       }
       cbConfig.selectedId = (char)arrby.readInt();
       cbConfig.msgIdMaxCount = (char)arrby.readInt();
       cbConfig.msgIdCount = arrby.readInt();
       byte[] arrby2 = cbConfig.msgIdMaxCount > '\u0000' ? new byte[cbConfig.msgIdMaxCount * 2] : new byte[100];
       if (cbConfig.msgIdCount > 100) {
           Rlog.d((String)"RILJ", (String)"No of CBMID Exceeded ");
       }
       cbConfig.msgIDs = new short[cbConfig.msgIdCount];
       arrby = arrby.readString();
       Rlog.d((String)"RILJ", (String)("ENABLED:" + cbConfig.bCBEnabled + ", selectedId:" + cbConfig.selectedId + ", msgIdCount:" + cbConfig.msgIdCount + ", msgIdMaxCount:" + cbConfig.msgIdMaxCount));
       if (arrby == null) {
           Rlog.d((String)"RILJ", (String)"MessageIDs String is NULL");
           return cbConfig;
       }
       Rlog.d((String)"RILJ", (String)(", MessageIDs:" + (String)arrby));
       arrby = IccUtils.hexStringToBytes((String)arrby);
       n2 = 0;
       while (n2 < cbConfig.msgIdCount) {
           byte by = arrby[n];
           byte by2 = arrby[n + 1];
           cbConfig.msgIDs[n2] = (short)((by & 255) << 8 | by2 & 255);
           n += 2;
           ++n2;
       }
       return cbConfig;
   }

   private Object responseCdmaBroadcastConfig(Parcel arrn) {
       int[] arrn2;
       int n = arrn.readInt();
       if (n == 0) {
           arrn = new int[94];
           arrn[0] = 31;
           n = 1;
           do {
               arrn2 = arrn;
               if (n < 94) {
                   arrn[n + 0] = n / 3;
                   arrn[n + 1] = 1;
                   arrn[n + 2] = 0;
                   n += 3;
                   continue;
               }
               break;
               break;
           } while (true);
       } else {
           int n2 = n * 3 + 1;
           int[] arrn3 = new int[n2];
           arrn3[0] = n;
           n = 1;
           do {
               arrn2 = arrn3;
               if (n >= n2) break;
               arrn3[n] = arrn.readInt();
               ++n;
           } while (true);
       }
       return arrn2;
   }

   private ArrayList<CdmaInformationRecords> responseCdmaInformationRecord(Parcel parcel) {
       int n = parcel.readInt();
       ArrayList<CdmaInformationRecords> arrayList = new ArrayList<CdmaInformationRecords>(n);
       for (int i = 0; i < n; ++i) {
           arrayList.add(new CdmaInformationRecords(parcel));
       }
       return arrayList;
   }

   private Object responseCdmaSms(Parcel parcel) {
       return SmsMessage.newFromParcel(parcel);
   }

   private ArrayList<CellInfo> responseCellInfoList(Parcel parcel) {
       int n = parcel.readInt();
       ArrayList<CellInfo> arrayList = new ArrayList<CellInfo>(n);
       for (int i = 0; i < n; ++i) {
           arrayList.add((CellInfo)CellInfo.CREATOR.createFromParcel(parcel));
       }
       return arrayList;
   }

   private Object responseCellList(Parcel parcel) {
       int n = parcel.readInt();
       ArrayList<NeighboringCellInfo> arrayList = new ArrayList<NeighboringCellInfo>();
       int[] arrn = SubscriptionManager.getSubId((int)this.mInstanceId);
       int n2 = ((TelephonyManager)this.mContext.getSystemService("phone")).getDataNetworkType(arrn[0]);
       if (n2 != 0) {
           for (int i = 0; i < n; ++i) {
               arrayList.add(new NeighboringCellInfo(parcel.readInt(), parcel.readString(), n2));
           }
       }
       return arrayList;
   }

   private Object responseDataCallList(Parcel parcel) {
       int n = parcel.readInt();
       int n2 = parcel.readInt();
       this.riljLog("responseDataCallList ver=" + n + " num=" + n2);
       ArrayList<DataCallResponse> arrayList = new ArrayList<DataCallResponse>(n2);
       for (int i = 0; i < n2; ++i) {
           arrayList.add(this.getDataCallResponse(parcel, n));
       }
       return arrayList;
   }

   private Object responseGetPreferredNetworkType(Parcel arrn) {
       if ((arrn = (int[])this.responseInts((Parcel)arrn)).length >= 1) {
           this.mPreferredNetworkType = arrn[0];
           this.setInitialPhoneType(this.mPreferredNetworkType);
       }
       return arrn;
   }

   /*
    * Enabled aggressive block sorting
    */
   private Object responseGmsBroadcastConfig(Parcel parcel) {
       int n = parcel.readInt();
       ArrayList<SmsBroadcastConfigInfo> arrayList = new ArrayList<SmsBroadcastConfigInfo>(n);
       int n2 = 0;
       while (n2 < n) {
           int n3 = parcel.readInt();
           int n4 = parcel.readInt();
           int n5 = parcel.readInt();
           int n6 = parcel.readInt();
           boolean bl = parcel.readInt() == 1;
           arrayList.add(new SmsBroadcastConfigInfo(n3, n4, n5, n6, bl));
           ++n2;
       }
       return arrayList;
   }

   /*
    * Enabled aggressive block sorting
    */
   private Object responseHardwareConfig(Parcel parcel) {
       int n = parcel.readInt();
       ArrayList<HardwareConfig> arrayList = new ArrayList<HardwareConfig>(n);
       int n2 = 0;
       while (n2 < n) {
           HardwareConfig hardwareConfig;
           int n3 = parcel.readInt();
           switch (n3) {
               default: {
                   throw new RuntimeException("RIL_REQUEST_GET_HARDWARE_CONFIG invalid hardward type:" + n3);
               }
               case 0: {
                   hardwareConfig = new HardwareConfig(n3);
                   hardwareConfig.assignModem(parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
                   break;
               }
               case 1: {
                   hardwareConfig = new HardwareConfig(n3);
                   hardwareConfig.assignSim(parcel.readString(), parcel.readInt(), parcel.readString());
               }
           }
           arrayList.add(hardwareConfig);
           ++n2;
       }
       return arrayList;
   }

   private Object responseICC_IO(Parcel object) {
       int n = object.readInt();
       int n2 = object.readInt();
       String string = object.readString();
       object = "< iccIO:  0x" + Integer.toHexString(n) + " 0x" + Integer.toHexString(n2) + " ";
       if (!SHIP_BUILD) {
           (String)object + string;
       }
       return new IccIoResult(n, n2, string);
   }

   private Object responseICC_IOBase64(Parcel parcel) {
       return new IccIoResult(parcel.readInt(), parcel.readInt(), Base64.decode((String)parcel.readString(), (int)0));
   }

   private Object responseIccCardStatus(Parcel parcel) {
       int n;
       IccCardStatus iccCardStatus = new IccCardStatus();
       iccCardStatus.setCardState(parcel.readInt());
       iccCardStatus.setUniversalPinState(parcel.readInt());
       iccCardStatus.mGsmUmtsSubscriptionAppIndex = parcel.readInt();
       iccCardStatus.mCdmaSubscriptionAppIndex = parcel.readInt();
       iccCardStatus.mImsSubscriptionAppIndex = parcel.readInt();
       int n2 = n = parcel.readInt();
       if (n > 8) {
           n2 = 8;
       }
       iccCardStatus.mApplications = new IccCardApplicationStatus[n2];
       for (n = 0; n < n2; ++n) {
           IccCardApplicationStatus iccCardApplicationStatus = new IccCardApplicationStatus();
           iccCardApplicationStatus.app_type = iccCardApplicationStatus.AppTypeFromRILInt(parcel.readInt());
           iccCardApplicationStatus.app_state = iccCardApplicationStatus.AppStateFromRILInt(parcel.readInt());
           iccCardApplicationStatus.perso_substate = iccCardApplicationStatus.PersoSubstateFromRILInt(parcel.readInt());
           iccCardApplicationStatus.aid = parcel.readString();
           iccCardApplicationStatus.app_label = parcel.readString();
           iccCardApplicationStatus.pin1_replaced = parcel.readInt();
           iccCardApplicationStatus.pin1 = iccCardApplicationStatus.PinStateFromRILInt(parcel.readInt());
           iccCardApplicationStatus.pin2 = iccCardApplicationStatus.PinStateFromRILInt(parcel.readInt());
           iccCardApplicationStatus.pin1_num_retries = parcel.readInt();
           iccCardApplicationStatus.puk1_num_retries = parcel.readInt();
           iccCardApplicationStatus.pin2_num_retries = parcel.readInt();
           iccCardApplicationStatus.puk2_num_retries = parcel.readInt();
           iccCardApplicationStatus.perso_unblock_retries = parcel.readInt();
           iccCardStatus.mApplications[n] = iccCardApplicationStatus;
       }
       return iccCardStatus;
   }

   private Object responseInts(Parcel parcel) {
       int n = parcel.readInt();
       int[] arrn = new int[n];
       for (int i = 0; i < n; ++i) {
           arrn[i] = parcel.readInt();
       }
       return arrn;
   }

   private Object responseNaf(Parcel parcel) {
       return IccUtils.hexStringToBytes(parcel.readString());
   }

   /*
    * Enabled aggressive block sorting
    */
   private Object responseOperatorInfos(Parcel arrayList) {
       String[] arrstring = (String[])this.responseStrings((Parcel)arrayList);
       if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_UseRatInfoDuringPlmnSelection") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_DisplayRatInfoInManualNetSearchList")) {
           if (arrstring.length % 6 != 0) {
               throw new RuntimeException("RIL_REQUEST_QUERY_AVAILABLE_NETWORKS: invalid response. Got " + arrstring.length + " strings, expected multible of 6");
           }
           ArrayList<OperatorInfo> arrayList2 = new ArrayList<OperatorInfo>(arrstring.length / 6);
           int n = 0;
           do {
               arrayList = arrayList2;
               if (n >= arrstring.length) return arrayList;
               {
                   arrayList2.add(new OperatorInfo(arrstring[n + 0], arrstring[n + 1], arrstring[n + 2], arrstring[n + 3], arrstring[n + 4], arrstring[n + 5]));
                   Rlog.d((String)"RILJ", (String)("Add OperatorInfo is:" + arrstring[n + 0] + " " + arrstring[n + 1] + " " + arrstring[n + 2] + " " + arrstring[n + 3] + " " + arrstring[n + 4] + " " + arrstring[n + 5] + " "));
                   n += 6;
                   continue;
               }
               break;
           } while (true);
       } else {
           if (arrstring.length % 6 != 0) {
               throw new RuntimeException("RIL_REQUEST_QUERY_AVAILABLE_NETWORKS: invalid response. Got " + arrstring.length + " strings, expected multible of 6");
           }
           ArrayList<OperatorInfo> arrayList3 = new ArrayList<OperatorInfo>(arrstring.length / 6);
           HashSet<String> hashSet = new HashSet<String>(arrstring.length / 6);
           String string = TelephonyManager.getTelephonyProperty((int)this.mInstanceId, (String)"gsm.sim.operator.numeric", (String)"");
           String string2 = TelephonyManager.getTelephonyProperty((int)this.mInstanceId, (String)"gsm.sim.operator.alpha", (String)"");
           String string3 = TelephonyManager.getTelephonyProperty((int)this.mInstanceId, (String)"gsm.operator.isroaming", (String)"");
           int n = 0;
           do {
               arrayList = arrayList3;
               if (n >= arrstring.length) return arrayList;
               if (!hashSet.contains(arrstring[n + 2])) {
                   hashSet.add(arrstring[n + 2]);
                   if ("45400".equals(arrstring[n + 2]) || "45402".equals(arrstring[n + 2]) || "45410".equals(arrstring[n + 2]) || "45418".equals(arrstring[n + 2]) && "false".equals(string3)) {
                       Rlog.d((String)"RILJ", (String)("CSL Network, SPN sholud be displayed instead of PLMN : " + string + "SPN : " + string2));
                       if ("45400".equals(string) || "45402".equals(string) || "45410".equals(string) || "45418".equals(string)) {
                           arrstring[n + 0] = string2;
                           arrstring[n + 1] = arrstring[n + 0];
                       }
                   }
                   if ("45416".equals(arrstring[n + 2]) || "45419".equals(arrstring[n + 2]) && "false".equals(string3)) {
                       Rlog.d((String)"RILJ", (String)("PCCW-HKT Network, SPN sholud be displayed instead of PLMN : " + string + "SPN : " + string2));
                       if ("45416".equals(string) || "45419".equals(string)) {
                           arrstring[n + 0] = string2;
                           arrstring[n + 1] = arrstring[n + 0];
                       }
                   }
                   if ("46697".equals(arrstring[n + 2]) && "false".equals(string3)) {
                       Rlog.d((String)"RILJ", (String)("APT Network, SPN sholud be displayed instead of PLMN : " + string + "SPN : " + string2));
                       if ("46605".equals(string)) {
                           arrstring[n + 0] = string2;
                           arrstring[n + 1] = arrstring[n + 0];
                       }
                   }
                   arrayList3.add(new OperatorInfo(arrstring[n + 0], arrstring[n + 1], arrstring[n + 2], arrstring[n + 3], arrstring[n + 4], arrstring[n + 5]));
                   Rlog.d((String)"RILJ", (String)("Add OperatorInfo is:" + arrstring[n + 0] + " " + arrstring[n + 1] + " " + arrstring[n + 2] + " " + arrstring[n + 3] + " " + arrstring[n + 4] + " " + arrstring[n + 5] + " "));
               }
               n += 6;
           } while (true);
       }
   }

   private Object responsePreferredNetworkList(Parcel parcel) {
       int n = parcel.readInt();
       Rlog.d((String)"RILJ", (String)("number of network list = " + n));
       ArrayList<PreferredNetworkListInfo> arrayList = new ArrayList<PreferredNetworkListInfo>(n);
       for (int i = 0; i < n; ++i) {
           PreferredNetworkListInfo preferredNetworkListInfo = new PreferredNetworkListInfo();
           preferredNetworkListInfo.mIndex = parcel.readInt();
           preferredNetworkListInfo.mOperator = parcel.readString();
           preferredNetworkListInfo.mPlmn = parcel.readString();
           preferredNetworkListInfo.mGsmAct = parcel.readInt();
           preferredNetworkListInfo.mGsmCompactAct = parcel.readInt();
           preferredNetworkListInfo.mUtranAct = parcel.readInt();
           preferredNetworkListInfo.mMode = parcel.readInt();
           arrayList.add(preferredNetworkListInfo);
       }
       return arrayList;
   }

   private Object responseRadioCapability(Parcel parcel) {
       int n = parcel.readInt();
       int n2 = parcel.readInt();
       int n3 = parcel.readInt();
       int n4 = parcel.readInt();
       String string = parcel.readString();
       int n5 = parcel.readInt();
       this.riljLog("responseRadioCapability: version= " + n + ", session=" + n2 + ", phase=" + n3 + ", rat=" + n4 + ", logicModemUuid=" + string + ", status=" + n5);
       return new RadioCapability(this.mInstanceId, n2, n3, n4, string, n5);
   }

   private Object responseRaw(Parcel parcel) {
       return parcel.createByteArray();
   }

   private Object responseSIM_LockInfo(Parcel parcel) {
       int n = parcel.readInt();
       int n2 = parcel.readInt();
       int n3 = parcel.readInt();
       int n4 = parcel.readInt();
       Rlog.i((String)"RILJ", (String)("num:" + n + " lock_type:" + n2 + " lock_key:" + n3 + " num_of_retry:" + n4));
       return new SimLockInfoResult(n, n2, n3, n4);
   }

   private Object responseSIM_PB(Parcel parcel) {
       int[] arrn = new int[3];
       int[] arrn2 = new int[3];
       String[] arrstring = new String[3];
       int[] arrn3 = new int[5];
       int[] arrn4 = new int[5];
       String[] arrstring2 = new String[5];
       parcel.readIntArray(arrn);
       parcel.readIntArray(arrn2);
       parcel.readStringArray(arrstring);
       if (!SHIP_BUILD) {
           Rlog.i((String)"RILJ", (String)("alphaTag is " + arrstring[0]));
       }
       if (!SHIP_BUILD) {
           Rlog.i((String)"RILJ", (String)("SNE is " + arrstring[1]));
       }
       if (!SHIP_BUILD) {
           Rlog.i((String)"RILJ", (String)("email is " + arrstring[2]));
       }
       parcel.readIntArray(arrn3);
       Rlog.i((String)"RILJ", (String)("lengthNumber is " + arrn3[0]));
       parcel.readIntArray(arrn4);
       parcel.readStringArray(arrstring2);
       if (!SHIP_BUILD) {
           Rlog.i((String)"RILJ", (String)("number is " + arrstring2[0]));
       }
       if (!SHIP_BUILD) {
           Rlog.i((String)"RILJ", (String)("ANR is " + arrstring2[1]));
       }
       return new SimPBEntryResult(arrn, arrn2, arrstring, arrn3, arrn4, arrstring2, parcel.readInt(), parcel.readInt());
   }

   private Object responseSMS(Parcel parcel) {
       return new SmsResponse(parcel.readInt(), parcel.readString(), parcel.readInt());
   }

   private Object responseSSReleaseCompleteNotification(Parcel parcel) {
       SSReleaseCompleteNotification sSReleaseCompleteNotification = new SSReleaseCompleteNotification();
       Rlog.i((String)"RILJ", (String)"responseSSReleaseCompleteNotification()");
       sSReleaseCompleteNotification.size = parcel.readInt();
       sSReleaseCompleteNotification.dataLen = parcel.readInt();
       sSReleaseCompleteNotification.params = parcel.readInt();
       sSReleaseCompleteNotification.status = parcel.readInt();
       sSReleaseCompleteNotification.data = parcel.readString();
       Rlog.i((String)"RILJ", (String)("notification.data = " + sSReleaseCompleteNotification.data));
       return sSReleaseCompleteNotification;
   }

   private Object responseSignalStrength(Parcel parcel) {
       return SignalStrength.makeSignalStrengthFromRilParcel((Parcel)parcel);
   }

   private Object responseSimPowerDone(Parcel parcel) {
       Rlog.d((String)"RILJ", (String)"ResponseSimPowerDone");
       int n = parcel.readInt();
       int[] arrn = new int[n];
       for (int i = 0; i < n; ++i) {
           arrn[i] = parcel.readInt();
       }
       Rlog.d((String)"RILJ", (String)("ResponseSimPowerDone : " + arrn[0]));
       return arrn[0];
   }

   private Object responseSimRefresh(Parcel parcel) {
       IccRefreshResponse iccRefreshResponse = new IccRefreshResponse();
       iccRefreshResponse.refreshResult = parcel.readInt();
       iccRefreshResponse.efId = parcel.readInt();
       return iccRefreshResponse;
   }

   private Object responseSsData(Parcel parcel) {
       SsData ssData = new SsData();
       ssData.serviceType = ssData.ServiceTypeFromRILInt(parcel.readInt());
       ssData.requestType = ssData.RequestTypeFromRILInt(parcel.readInt());
       ssData.teleserviceType = ssData.TeleserviceTypeFromRILInt(parcel.readInt());
       ssData.serviceClass = parcel.readInt();
       ssData.result = parcel.readInt();
       int n = parcel.readInt();
       if (ssData.serviceType.isTypeCF() && ssData.requestType.isTypeInterrogation()) {
           ssData.cfInfo = new CallForwardInfo[n];
           for (int i = 0; i < n; ++i) {
               ssData.cfInfo[i] = new CallForwardInfo();
               ssData.cfInfo[i].status = parcel.readInt();
               ssData.cfInfo[i].reason = parcel.readInt();
               ssData.cfInfo[i].serviceClass = parcel.readInt();
               ssData.cfInfo[i].toa = parcel.readInt();
               ssData.cfInfo[i].number = parcel.readString();
               ssData.cfInfo[i].timeSeconds = parcel.readInt();
               this.riljLog("[SS Data] CF Info " + i + " : " + ssData.cfInfo[i]);
           }
       } else {
           ssData.ssInfo = new int[n];
           for (int i = 0; i < n; ++i) {
               ssData.ssInfo[i] = parcel.readInt();
               this.riljLog("[SS Data] SS Info " + i + " : " + ssData.ssInfo[i]);
           }
       }
       return ssData;
   }

   private Object responseString(Parcel parcel) {
       return parcel.readString();
   }

   private Object responseStrings(Parcel parcel) {
       return parcel.readStringArray();
   }

   private Object responseSuppServiceNotification(Parcel parcel) {
       SuppServiceNotification suppServiceNotification = new SuppServiceNotification();
       suppServiceNotification.notificationType = parcel.readInt();
       suppServiceNotification.code = parcel.readInt();
       suppServiceNotification.index = parcel.readInt();
       suppServiceNotification.type = parcel.readInt();
       suppServiceNotification.mtConference = 0;
       if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportVolte") && "VZW-CDMA".equals("EUR") && suppServiceNotification.code == 3) {
           suppServiceNotification.mtConference = parcel.readInt();
       }
       suppServiceNotification.number = parcel.readString();
       return suppServiceNotification;
   }

   /*
    * Enabled force condition propagation
    * Lifted jumps to return sites
    */
   static String responseToString(int n) {
       switch (n) {
           default: {
               return "<unknown response>";
           }
           case 1000: {
               return "UNSOL_RESPONSE_RADIO_STATE_CHANGED";
           }
           case 1001: {
               return "UNSOL_RESPONSE_CALL_STATE_CHANGED";
           }
           case 1002: {
               return "UNSOL_RESPONSE_VOICE_NETWORK_STATE_CHANGED";
           }
           case 1003: {
               return "UNSOL_RESPONSE_NEW_SMS";
           }
           case 1004: {
               return "UNSOL_RESPONSE_NEW_SMS_STATUS_REPORT";
           }
           case 1005: {
               return "UNSOL_RESPONSE_NEW_SMS_ON_SIM";
           }
           case 1006: {
               return "UNSOL_ON_USSD";
           }
           case 1007: {
               return "UNSOL_ON_USSD_REQUEST";
           }
           case 1008: {
               return "UNSOL_NITZ_TIME_RECEIVED";
           }
           case 1009: {
               return "UNSOL_SIGNAL_STRENGTH";
           }
           case 1010: {
               return "UNSOL_DATA_CALL_LIST_CHANGED";
           }
           case 1011: {
               return "UNSOL_SUPP_SVC_NOTIFICATION";
           }
           case 1012: {
               return "UNSOL_STK_SESSION_END";
           }
           case 1013: {
               return "UNSOL_STK_PROACTIVE_COMMAND";
           }
           case 1014: {
               return "UNSOL_STK_EVENT_NOTIFY";
           }
           case 1015: {
               return "UNSOL_STK_CALL_SETUP";
           }
           case 1016: {
               return "UNSOL_SIM_SMS_STORAGE_FULL";
           }
           case 1017: {
               return "UNSOL_SIM_REFRESH";
           }
           case 1018: {
               return "UNSOL_CALL_RING";
           }
           case 1019: {
               return "UNSOL_RESPONSE_SIM_STATUS_CHANGED";
           }
           case 1020: {
               return "UNSOL_RESPONSE_CDMA_NEW_SMS";
           }
           case 1021: {
               return "UNSOL_RESPONSE_NEW_BROADCAST_SMS";
           }
           case 1022: {
               return "UNSOL_CDMA_RUIM_SMS_STORAGE_FULL";
           }
           case 1023: {
               return "UNSOL_RESTRICTED_STATE_CHANGED";
           }
           case 1024: {
               return "UNSOL_ENTER_EMERGENCY_CALLBACK_MODE";
           }
           case 1025: {
               return "UNSOL_CDMA_CALL_WAITING";
           }
           case 1026: {
               return "UNSOL_CDMA_OTA_PROVISION_STATUS";
           }
           case 1027: {
               return "UNSOL_CDMA_INFO_REC";
           }
           case 1028: {
               return "UNSOL_OEM_HOOK_RAW";
           }
           case 1029: {
               return "UNSOL_RINGBACK_TONE";
           }
           case 1030: {
               return "UNSOL_RESEND_INCALL_MUTE";
           }
           case 1031: {
               return "CDMA_SUBSCRIPTION_SOURCE_CHANGED";
           }
           case 1032: {
               return "UNSOL_CDMA_PRL_CHANGED";
           }
           case 1033: {
               return "UNSOL_EXIT_EMERGENCY_CALLBACK_MODE";
           }
           case 1034: {
               return "UNSOL_RIL_CONNECTED";
           }
           case 1035: {
               return "UNSOL_VOICE_RADIO_TECH_CHANGED";
           }
           case 1036: {
               return "UNSOL_CELL_INFO_LIST";
           }
           case 1037: {
               return "UNSOL_RESPONSE_IMS_NETWORK_STATE_CHANGED";
           }
           case 1038: {
               return "RIL_UNSOL_UICC_SUBSCRIPTION_STATUS_CHANGED";
           }
           case 1039: {
               return "UNSOL_SRVCC_STATE_NOTIFY";
           }
           case 1040: {
               return "RIL_UNSOL_HARDWARE_CONFIG_CHANGED";
           }
           case 1042: {
               return "RIL_UNSOL_RADIO_CAPABILITY";
           }
           case 1043: {
               return "UNSOL_ON_SS";
           }
           case 1044: {
               return "UNSOL_STK_CC_ALPHA_NOTIFY";
           }
           case 11000: {
               return "UNSOL_RESPONSE_NEW_CB_MSG";
           }
           case 11001: {
               return "UNSOL_RELEASE_COMPLETE_MESSAGE";
           }
           case 11002: {
               return "UNSOL_STK_SEND_SMS_RESULT";
           }
           case 11003: {
               return "UNSOL_STK_CALL_CONTROL_RESULT";
           }
           case 11008: {
               return "UNSOL_DEVICE_READY_NOTI";
           }
           case 11009: {
               return "UNSOL_GPS_NOTI";
           }
           case 11010: {
               return "UNSOL_AM";
           }
           case 11013: {
               return "UNSOL_SAP";
           }
           case 11020: {
               return "UNSOL_UART";
           }
           case 11021: {
               return "UNSOL_SIM_PB_READY";
           }
           case 11024: {
               return "UNSOL_VE";
           }
           case 11026: {
               return "UNSOL_FACTORY_AM";
           }
           case 11027: {
               return "UNSOL_IMS_REGISTRATION_STATE_CHANGED";
           }
           case 11028: {
               return "UNSOL_MODIFY_CALL";
           }
           case 11030: {
               return "UNSOL_CS_FALLBACK";
           }
           case 11032: {
               return "UNSOL_VOICE_SYSTEM_ID";
           }
           case 11034: {
               return "UNSOL_IMS_RETRYOVER";
           }
           case 11035: {
               return "UNSOL_PB_INIT_COMPLETE";
           }
           case 11037: {
               return "UNSOL_HYSTERESIS_DCN";
           }
           case 11038: {
               return "UNSOL_CP_POSITION";
           }
           case 11043: {
               return "UNSOL_HOME_NETWORK_NOTI";
           }
           case 11054: {
               return "UNSOL_STK_CALL_STATUS";
           }
           case 11056: {
               return "UNSOL_MODEM_CAP";
           }
           case 11060: {
               return "UNSOL_DUN";
           }
           case 11061: {
               return "UNSOL_IMS_PREFERENCE_CHANGED";
           }
           case 11062: {
               return "UNSOL_SIM_APPLICATION_REFRESH";
           }
           case 11063: {
               return "UNSOL_UICC_APPLICATION_STATUS";
           }
           case 11066:
       }
       return "UNSOL_SIM_ICCID_NOTI";
   }

   /*
    * Enabled aggressive block sorting
    * Enabled unnecessary exception pruning
    * Enabled aggressive exception aggregation
    */
   private Object responseUSSD(Parcel parcel) {
       int n = parcel.readInt();
       int n2 = parcel.readInt();
       Rlog.d((String)"RILJ", (String)("responseUSSD - num " + n));
       String[] arrstring = new String[n];
       int n3 = 0;
       while (n3 < n) {
           if (n2 == 148 && n3 > 0) {
               Object object = IccUtils.hexStringToBytes(parcel.readString());
               try {
                   String string;
                   arrstring[n3] = string = new String((byte[])object, "EUC_KR");
                   object = new Object();
                   Rlog.d((String)"RILJ", (String)object.append("responseUSSD :: USSD_DCS_KS5601, response").append(arrstring[n3]).toString());
               }
               catch (UnsupportedEncodingException var3_7) {
                   arrstring[n3] = "";
               }
           } else {
               arrstring[n3] = parcel.readString();
           }
           ++n3;
       }
       return arrstring;
   }

   private Object responseVoid(Parcel parcel) {
       return null;
   }

   /*
    * Unable to fully structure code
    * Enabled aggressive block sorting
    * Lifted jumps to return sites
    */
   static String retToString(int var0, Object var1_1) {
       if (var1_1 == null) {
           return "";
       }
       switch (var0) {
           case 105:
           case 109:
           case 1013:
           case 1014:
           case 1015:
           case 1027: {
               if (!RIL.SHIP_BUILD) break;
               return "";
           }
           case 11:
           case 38:
           case 39:
           case 115:
           case 117: {
               return "";
           }
           case 98: {
               if (!RIL.SHIP_BUILD) break;
               return "{xx,xx,xx,xx,xx}";
           }
           case 22: {
               if (RIL.SHIP_BUILD) {
                   return "{xx,xx,xx,xx}";
               }
           }
           case 95: {
               if (!RIL.SHIP_BUILD) break;
               return "{xx,xx,xx,xx,xx}";
           }
       }
       if (var1_1 instanceof int[]) {
           var2_2 = (int[])var1_1;
           var4_9 = var2_2.length;
           var1_1 = new StringBuilder("{");
           if (var4_9 > 0) {
               var1_1.append(var2_2[0]);
               for (var0 = 0 + 1; var0 < var4_9; ++var0) {
                   var1_1.append(", ").append(var2_2[var0]);
               }
           }
           var1_1.append("}");
           return var1_1.toString();
       }
       if (var1_1 instanceof String[]) {
           var2_3 = (String[])var1_1;
           var4_10 = var2_3.length;
           var1_1 = new StringBuilder("{");
           if (var4_10 > 0) {
               var1_1.append(var2_3[0]);
               for (var0 = 0 + 1; var0 < var4_10; ++var0) {
                   var1_1.append(", ").append(var2_3[var0]);
               }
           }
           var1_1.append("}");
           return var1_1.toString();
       }
       if (var0 == 9) {
           var2_4 = (ArrayList)var1_1;
           var1_1 = new StringBuilder(" ");
           var2_4 = var2_4.iterator();
           do {
               if (!var2_4.hasNext()) {
                   return var1_1.toString();
               }
               var3_12 = (DriverCall)var2_4.next();
               var1_1.append("[").append(var3_12).append("] ");
           } while (true);
       }
       if (var0 == 75) {
           var2_5 = (ArrayList)var1_1;
           var1_1 = new StringBuilder(" ");
           var2_5 = var2_5.iterator();
           do {
               if (!var2_5.hasNext()) {
                   return var1_1.toString();
               }
               var1_1.append((Object)((NeighboringCellInfo)var2_5.next())).append(" ");
           } while (true);
       }
       if (var0 == 124) {
           var2_6 = (ArrayList)var1_1;
           var1_1 = new StringBuilder(" ");
           var3_13 = var2_6.iterator();
           do {
               if (!var3_13.hasNext()) {
                   return var1_1.toString();
               }
               var2_6 = (HardwareConfig)var3_13.next();
               var1_1.append("[").append(var2_6).append("] ");
           } while (true);
       }
       if (var0 == 59) {
           var2_7 = var1_1.toString();
           var1_1 = var2_7;
           if (RIL.SHIP_BUILD == false) return var1_1;
           var1_1 = var2_7;
           if (var2_7.length() <= 5) return var1_1;
           return var2_7.substring(0, 5) + "...";
       }
       if (var0 != 33) {
           return var1_1.toString();
       }
       var2_8 = (CallForwardInfo[])var1_1;
       var5_14 = var2_8.length;
       var1_1 = new StringBuilder("[");
       if (!RIL.SHIP_BUILD) ** GOTO lbl89
       if (var5_14 <= 0) ** GOTO lbl105
       var0 = 0 + 1;
       if (var2_8[0].status == 1) {
           var1_1.append(" active ");
       } else {
           var1_1.append(" not active ");
       }
       ** GOTO lbl96
lbl89: // 1 sources:
       if (var5_14 > 0) {
           var1_1.append(var2_8[0].toString());
           for (var0 = 0 + 1; var0 < var5_14; ++var0) {
               var1_1.append("] [").append(var2_8[var0].toString());
           }
       }
       ** GOTO lbl105
lbl96: // 4 sources:
       while (var0 < var5_14) {
           var4_11 = var0 + 1;
           if (var2_8[var0].status == 1) {
               var1_1.append("] [").append(" active ");
               var0 = var4_11;
               continue;
           }
           var1_1.append("] [").append(" not active ");
           var0 = var4_11;
       }
lbl105: // 3 sources:
       var1_1.append("]");
       return var1_1.toString();
   }

   /*
    * Enabled aggressive block sorting
    */
   private void riljLog(String string) {
       StringBuilder stringBuilder = new StringBuilder().append(string);
       string = this.mInstanceId != null ? " [SUB" + this.mInstanceId + "]" : "";
       Rlog.d((String)"RILJ", (String)stringBuilder.append(string).toString());
   }

   /*
    * Enabled aggressive block sorting
    */
   private void riljLogv(String string) {
       StringBuilder stringBuilder = new StringBuilder().append(string);
       string = this.mInstanceId != null ? " [SUB" + this.mInstanceId + "]" : "";
       Rlog.v((String)"RILJ", (String)stringBuilder.append(string).toString());
   }

   /*
    * Enabled aggressive block sorting
    */
   private void sendSafemode(boolean bl) {
       int n = 1;
       RILRequest rILRequest = RILRequest.obtain(10006, null);
       rILRequest.mParcel.writeInt(1);
       Parcel parcel = rILRequest.mParcel;
       if (!bl) {
           n = 0;
       }
       parcel.writeInt(n);
       rILRequest.mParcel.writeInt(0);
       this.riljLog(rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest) + " " + bl);
       this.send(rILRequest);
   }

   /*
    * Enabled aggressive block sorting
    */
   private void sendScreenState(boolean bl) {
       int n = 1;
       RILRequest rILRequest = RILRequest.obtain(61, null);
       rILRequest.mParcel.writeInt(1);
       Parcel parcel = rILRequest.mParcel;
       if (!bl) {
           n = 0;
       }
       parcel.writeInt(n);
       this.riljLog(rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest) + ": " + bl);
       this.send(rILRequest);
   }

   private void setInitialPhoneType(int n) {
       if ((n = TelephonyManager.getPhoneType((int)n)) != this.initPhoneType) {
           SystemProperties.set((String)"persist.radio.initphone-type", (String)String.valueOf(n));
           Rlog.d((String)"RILJ", (String)("Initial PhoneType is changed: " + this.initPhoneType + " -> " + n));
           this.initPhoneType = n;
       }
   }

   private void setPreferredData() {
   }

   /*
    * Enabled aggressive block sorting
    */
   private void switchToRadioState(CommandsInterface.RadioState object) {
       CommandsInterface.RadioState radioState = this.getRadioState();
       this.setRadioState((CommandsInterface.RadioState)((Object)object));
       String string = SystemProperties.get((String)"ro.csc.sales_code");
       object = SystemProperties.get((String)"persist.sys.bpmsetting.default", (String)"FirstTime");
       if (string != null && "COL".equals(string) && object != null && "FirstTime".equals(object)) {
           SystemProperties.set((String)"persist.sys.bpmsetting.enable", (String)"1");
           SystemProperties.set((String)"persist.sys.bpmsetting.default", (String)"1");
       }
       if (TelephonyManager.getDefault().getPhoneCount() <= 1) return;
       if (!("CHN".equals(string) || "CHC".equals(string) || "CHU".equals(string) || "CHM".equals(string) || "CTC".equals(string) || "BRI".equals(string) || "TGY".equals(string) || "CWT".equals(string) || "FET".equals(string) || "TWM".equals(string))) {
           if (!"CHZ".equals(string)) return;
       }
       if (!"Combination".equals("Sangria")) return;
       if (!this.getRadioState().isOn()) return;
       if (radioState.isOn()) return;
       object = SystemProperties.get((String)"persist.sys.bpmsetting.enable", (String)"X");
       if (object != null && "X".equals(object)) {
           this.riljLog("init Bpm");
           SystemProperties.set((String)"persist.sys.bpmsetting.enable", (String)"1");
           this.invokeOemRilRequestRaw(new byte[]{2, 96, 0, 5, 5}, null);
           return;
       }
       this.riljLog("init Bpm before set : " + (String)object);
   }

   /*
    * Unable to fully structure code
    * Enabled aggressive block sorting
    * Lifted jumps to return sites
    */
   private void testingETWS(SmsMessage var1_1) {
       var2_2 = IccUtils.hexStringToBytes("3000110011010D0A5BAE57CE770C531790E85C716CBF3044573065B9306757309707767A751F30025F37304463FA308C306B509930483066");
       var3_3 = IccUtils.hexStringToBytes("2000110011010D0A5BAE57CE770C531790E85C716CBF3044573065B9306757309707767A751F30025F37304463FA308C306B509930483066");
       var4_4 = IccUtils.hexStringToBytes("1000110011010D0A5BAE57CE770C531790E85C716CBF3044573065B9306757309707767A751F30025F37304463FA308C306B509930483066");
       var5_5 = IccUtils.hexStringToBytes("4000110011010D0A5BAE57CE770C531790E85C716CBF3044573065B9306757309707767A751F30025F37304463FA308C306B509930483066");
       var6_6 = Integer.parseInt(var1_1.getMessageBody());
       this.riljLog("testingETWS");
       switch (var6_6) {
           case 11100: {
               var1_1 = var2_2;
               ** GOTO lbl19
           }
           case 21100: {
               var1_1 = var3_3;
               ** GOTO lbl19
           }
           case 31100: {
               var1_1 = var4_4;
               ** GOTO lbl19
           }
           case 41100: {
               var1_1 = var5_5;
lbl19: // 4 sources:
               if (this.mGsmBroadcastSmsRegistrant == null) return;
               this.mGsmBroadcastSmsRegistrant.notifyRegistrant(new AsyncResult((Object)null, (Object)var1_1, null));
           }
       }
   }

   /*
    * Unable to fully structure code
    * Enabled aggressive block sorting
    * Lifted jumps to return sites
    */
   private int translateStatus(int var1_1) {
       var2_3 = var3_2 = 1;
       switch (var1_1 & 7) {
           default: {
               var2_3 = var3_2;
               ** GOTO lbl11
           }
           case 3: {
               var2_3 = 0;
               ** GOTO lbl11
           }
           case 5: {
               var2_3 = 3;
           }
lbl11: // 4 sources:
           case 1:
           case 2:
           case 4:
           case 6: {
               return var2_3;
           }
           case 7:
       }
       return 2;
   }

   private void unsljLog(int n) {
       this.riljLog("[UNSL]< " + RIL.responseToString(n));
   }

   private void unsljLogMore(int n, String string) {
       this.riljLog("[UNSL]< " + RIL.responseToString(n) + " " + string);
   }

   private void unsljLogRet(int n, Object object) {
       this.riljLog("[UNSL]< " + RIL.responseToString(n) + " " + RIL.retToString(n, object));
   }

   private void unsljLogvRet(int n, Object object) {
       this.riljLogv("[UNSL]< " + RIL.responseToString(n) + " " + RIL.retToString(n, object));
   }

   /*
    * Enabled aggressive block sorting
    */
   private void updateScreenState() {
       int n = this.mDefaultDisplayState;
       if (this.mDefaultDisplay == null) {
           this.mDefaultDisplay = ((DisplayManager)this.mContext.getSystemService("display")).getDisplay(0);
           if (this.mDefaultDisplay == null) {
               this.riljLog("updateScreenState, mDefaultDisplay is null");
               return;
           }
       }
       this.mDefaultDisplayState = this.mDefaultDisplay.getState();
       if (this.mDefaultDisplayState == n) return;
       if (n != 2 && this.mDefaultDisplayState == 2) {
           this.sendScreenState(true);
           return;
       }
       if (n != 2) {
           if (n != 0) return;
       }
       if (this.mDefaultDisplayState == 2) return;
       this.sendScreenState(false);
   }

   @Override
   public void acceptCall(int n, Message object) {
       object = RILRequest.obtain(40, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + n);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.send((RILRequest)object);
   }

   @Override
   public void acceptCall(Message message) {
       this.acceptCall(0, message);
   }

   /*
    * Unable to fully structure code
    * Enabled aggressive block sorting
    * Enabled unnecessary exception pruning
    * Enabled aggressive exception aggregation
    * Lifted jumps to return sites
    */
   @Override
   public void accessPhoneBookEntry(int var1_1, int var2_2, int var3_3, AdnRecord var4_4, String var5_7, Message var6_8) {
       block26 : {
           block24 : {
               block23 : {
                   block22 : {
                       var14_10 = RILRequest.obtain(10011, (Message)var6_8);
                       this.riljLog(var14_10.serialString() + "> " + RIL.requestToString(var14_10.mRequest));
                       var11_11 = var4_4.mAlphaTag;
                       var17_12 = var4_4.mNumber;
                       var16_13 = var4_4.mEmails[0];
                       var7_14 = var4_4.mAnr;
                       var8_15 = var4_4.mAnrA;
                       var9_16 = var4_4.mAnrB;
                       var10_17 = var4_4.mAnrC;
                       var15_20 = var4_4.mSne;
                       var6_8 = var7_14;
                       if (var7_14.length() == 0) {
                           var6_8 = null;
                       }
                       var7_14 = var8_15;
                       if (var8_15.length() == 0) {
                           var7_14 = null;
                       }
                       var8_15 = var9_16;
                       if (var9_16.length() == 0) {
                           var8_15 = null;
                       }
                       var9_16 = var10_17;
                       if (var10_17.length() == 0) {
                           var9_16 = null;
                       }
                       var10_17 = new byte[]{};
                       var12_21 = new byte[]{};
                       var13_22 = new byte[]{};
                       var14_10.mParcel.writeInt(var1_1);
                       var14_10.mParcel.writeInt(var2_2);
                       var14_10.mParcel.writeInt(var3_3);
                       try {
                           GsmAlphabet.countGsmSeptets((CharSequence)var11_11, (boolean)true);
                           var1_1 = 1;
                       }
                       catch (Exception var4_5) {
                           var1_1 = 0;
                       }
                       if (var1_1 != 0) {
                           // empty if block
                       }
                       var4_4 = var10_17;
                       try {
                           var18_23 = var11_11.getBytes("ISO-10646-UCS-2");
                           var4_4 = var10_17;
                           var11_11 = new byte[var18_23.length - 2];
                           var1_1 = 0;
                       }
                       catch (Exception var10_18) {
                           var10_17 = var4_4;
                           break block22;
                       }
                       do {
                           var4_4 = var11_11;
                           var10_17 = var11_11;
                           if (var1_1 >= var18_23.length - 2) break;
                           var11_11[var1_1] = var18_23[var1_1 + 2];
                           ++var1_1;
                       } while (true);
                   }
                   for (var1_1 = 0; var1_1 < var10_17.length; ++var1_1) {
                       this.riljLog("name[" + var1_1 + " ] = " + var10_17[var1_1]);
                   }
                   var14_10.mParcel.writeByteArray(var10_17);
                   var14_10.mParcel.writeInt(var10_17.length);
                   var14_10.mParcel.writeInt(3);
                   var14_10.mParcel.writeString((String)var17_12);
                   if (false) {
                       var4_4 = var13_22;
                       try {
                           var17_12 = var16_13.getBytes("ISO-10646-UCS-2");
                           var4_4 = var13_22;
                           var11_11 = new byte[var17_12.length - 2];
                           var1_1 = 0;
                           break block23;
                       }
                       catch (Exception var10_19) {
                           var10_17 = var4_4;
                           break block24;
                       }
                   }
                   var4_4 = GsmAlphabet.stringToGsm8BitPacked((String)var16_13);
                   var14_10.mParcel.writeByteArray(var4_4);
                   var14_10.mParcel.writeInt(var4_4.length);
                   ** GOTO lbl89
               }
               do {
                   var4_4 = var11_11;
                   var10_17 = var11_11;
                   if (var1_1 >= var17_12.length - 2) break;
                   var11_11[var1_1] = var17_12[var1_1 + 2];
                   ++var1_1;
               } while (true);
           }
           this.riljLog("email = " + var16_13);
           var14_10.mParcel.writeByteArray(var10_17);
           var14_10.mParcel.writeInt(var10_17.length);
lbl89: // 2 sources:
           this.riljLog("anr = " + (String)var6_8);
           var14_10.mParcel.writeString((String)var6_8);
           var14_10.mParcel.writeString((String)var7_14);
           var14_10.mParcel.writeString((String)var8_15);
           var14_10.mParcel.writeString(var9_16);
           try {
               GsmAlphabet.countGsmSeptets((CharSequence)var15_20, (boolean)true);
           }
           catch (Exception var4_6) {}
           var4_4 = var12_21;
           try {
               var8_15 = var15_20.getBytes("ISO-10646-UCS-2");
               var4_4 = var12_21;
               var7_14 = new byte[var8_15.length - 2];
               var1_1 = 0;
           }
           catch (Exception var6_9) {
               var6_8 = var4_4;
               break block26;
           }
           do {
               var4_4 = var7_14;
               var6_8 = var7_14;
               if (var1_1 >= var8_15.length - 2) break;
               var7_14[var1_1] = var8_15[var1_1 + 2];
               ++var1_1;
           } while (true);
       }
       this.riljLog("sne = " + var15_20);
       var1_1 = 0;
       do {
           if (var1_1 >= var6_8.length) {
               var14_10.mParcel.writeByteArray(var6_8);
               var14_10.mParcel.writeInt(var6_8.length);
               var14_10.mParcel.writeInt(3);
               var14_10.mParcel.writeString(var5_7);
               this.send(var14_10);
               return;
           }
           this.riljLog("sne[" + var1_1 + " ] = " + var6_8[var1_1]);
           ++var1_1;
       } while (true);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void acknowledgeIncomingGsmSmsWithPdu(boolean bl, String string, Message object) {
       RILRequest rILRequest = RILRequest.obtain(106, (Message)object);
       rILRequest.mParcel.writeInt(2);
       Parcel parcel = rILRequest.mParcel;
       object = bl ? "1" : "0";
       parcel.writeString((String)object);
       rILRequest.mParcel.writeString(string);
       this.riljLog(rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest) + ' ' + bl + " [" + string + ']');
       this.send(rILRequest);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void acknowledgeLastIncomingCdmaSms(boolean bl, int n, Message message) {
       RILRequest rILRequest = RILRequest.obtain(88, message);
       message = rILRequest.mParcel;
       int n2 = bl ? 0 : 1;
       message.writeInt(n2);
       rILRequest.mParcel.writeInt(n);
       this.riljLog(rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest) + " " + bl + " " + n);
       this.send(rILRequest);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void acknowledgeLastIncomingGsmSms(boolean bl, int n, Message message) {
       RILRequest rILRequest = RILRequest.obtain(37, message);
       rILRequest.mParcel.writeInt(2);
       message = rILRequest.mParcel;
       int n2 = bl ? 1 : 0;
       message.writeInt(n2);
       rILRequest.mParcel.writeInt(n);
       this.riljLog(rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest) + " " + bl + " " + n);
       this.send(rILRequest);
   }

   @Override
   public void cancelPendingUssd(Message object) {
       object = RILRequest.obtain(30, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void changeBarringPassword(String string, String string2, String string3, Message object) {
       object = RILRequest.obtain(44, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(3);
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       object.mParcel.writeString(string3);
       this.send((RILRequest)object);
   }

   @Override
   public void changeBarringPassword(String string, String string2, String string3, String string4, Message object) {
       object = RILRequest.obtain(44, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(4);
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       object.mParcel.writeString(string3);
       object.mParcel.writeString(string4);
       this.send((RILRequest)object);
   }

   @Override
   public void changeIccPin(String string, String string2, Message message) {
       this.changeIccPinForApp(string, string2, null, message);
   }

   @Override
   public void changeIccPin2(String string, String string2, Message message) {
       this.changeIccPin2ForApp(string, string2, null, message);
   }

   @Override
   public void changeIccPin2ForApp(String string, String string2, String string3, Message object) {
       object = RILRequest.obtain(7, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(3);
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       object.mParcel.writeString(string3);
       this.send((RILRequest)object);
   }

   @Override
   public void changeIccPinForApp(String string, String string2, String string3, Message object) {
       object = RILRequest.obtain(6, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(3);
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       object.mParcel.writeString(string3);
       this.send((RILRequest)object);
   }

   @Override
   public void changeIccSimPerso(String string, String string2, Message object) {
       object = RILRequest.obtain(10017, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(2);
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       this.send((RILRequest)object);
   }

   @Override
   public void conference(Message object) {
       object = RILRequest.obtain(16, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void deactivateDataCall(int n, int n2, Message object) {
       object = RILRequest.obtain(41, (Message)object);
       object.mParcel.writeInt(2);
       object.mParcel.writeString(Integer.toString(n));
       object.mParcel.writeString(Integer.toString(n2));
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + n + " " + n2);
       this.send((RILRequest)object);
   }

   public void deflect(String string, Message object) {
       object = RILRequest.obtain(10002, (Message)object);
       object.mParcel.writeString(string);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void deleteSmsOnRuim(int n, Message object) {
       object = RILRequest.obtain(97, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.send((RILRequest)object);
   }

   @Override
   public void deleteSmsOnSim(int n, Message object) {
       object = RILRequest.obtain(64, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.send((RILRequest)object);
   }

   @Override
   public void dial(String string, int n, Message message) {
       this.dial(string, n, null, message);
   }

   @Override
   public void dial(String string, int n, UUSInfo uUSInfo, Message message) {
       this.dial(string, n, uUSInfo, null, message);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void dial(String string, int n, UUSInfo uUSInfo, CallDetails callDetails, Message object) {
       object = RILRequest.obtain(10, (Message)object);
       object.mParcel.writeString(string);
       object.mParcel.writeInt(n);
       if (callDetails != null) {
           object.mParcel.writeInt(callDetails.call_type);
           object.mParcel.writeInt(callDetails.call_domain);
           object.mParcel.writeString(callDetails.getCsvFromExtras());
       } else {
           object.mParcel.writeInt(0);
           object.mParcel.writeInt(1);
           object.mParcel.writeString("");
       }
       if (uUSInfo == null) {
           object.mParcel.writeInt(0);
       } else {
           object.mParcel.writeInt(1);
           object.mParcel.writeInt(uUSInfo.getType());
           object.mParcel.writeInt(uUSInfo.getDcs());
           object.mParcel.writeByteArray(uUSInfo.getUserData());
       }
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + callDetails);
       this.send((RILRequest)object);
   }

   @Override
   public void dialEmergencyCall(String string, int n, Message message) {
       this.dialEmergencyCall(string, n, null, message);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void dialEmergencyCall(String string, int n, CallDetails callDetails, Message object) {
       object = RILRequest.obtain(10001, (Message)object);
       object.mParcel.writeString(string);
       object.mParcel.writeInt(n);
       if (callDetails != null) {
           object.mParcel.writeInt(callDetails.call_type);
           object.mParcel.writeInt(callDetails.call_domain);
           object.mParcel.writeString("");
       } else {
           object.mParcel.writeInt(0);
           object.mParcel.writeInt(3);
           object.mParcel.writeString("");
       }
       object.mParcel.writeInt(0);
       if (callDetails != null) {
           this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + callDetails);
       } else {
           this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       }
       this.send((RILRequest)object);
   }

   /*
    * Enabled aggressive block sorting
    * Enabled unnecessary exception pruning
    * Enabled aggressive exception aggregation
    */
   public void dump(FileDescriptor sparseArray, PrintWriter printWriter, String[] object) {
       printWriter.println("RIL: " + this);
       printWriter.println(" mSocket=" + (Object)this.mSocket);
       printWriter.println(" mSenderThread=" + (Object)this.mSenderThread);
       printWriter.println(" mSender=" + this.mSender);
       printWriter.println(" mReceiverThread=" + this.mReceiverThread);
       printWriter.println(" mReceiver=" + this.mReceiver);
       printWriter.println(" mWakeLock=" + (Object)this.mWakeLock);
       printWriter.println(" mWakeLockTimeout=" + this.mWakeLockTimeout);
       sparseArray = this.mRequestList;
       synchronized (sparseArray) {
           StringBuilder stringBuilder;
           object = this.mWakeLock;
           synchronized (object) {
               stringBuilder = new StringBuilder();
               printWriter.println(stringBuilder.append(" mWakeLockCount=").append(this.mWakeLockCount).toString());
           }
           int n = this.mRequestList.size();
           object = new Object();
           printWriter.println(object.append(" mRequestList count=").append(n).toString());
           int n2 = 0;
           do {
               if (n2 >= n) {
                   printWriter.println(" mLastNITZTimeInfo=" + this.mLastNITZTimeInfo);
                   printWriter.println(" mTestingEmergencyCall=" + this.mTestingEmergencyCall.get());
                   return;
               }
               object = (RILRequest)this.mRequestList.valueAt(n2);
               stringBuilder = new StringBuilder();
               printWriter.println(stringBuilder.append("  [").append(object.mSerial).append("] ").append(RIL.requestToString(object.mRequest)).toString());
               ++n2;
           } while (true);
       }
   }

   @Override
   public void exitEmergencyCallbackMode(Message object) {
       object = RILRequest.obtain(99, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void explicitCallTransfer(Message object) {
       object = RILRequest.obtain(72, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getAvailableNetworks(Message object) {
       object = RILRequest.obtain(48, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getBasebandVersion(Message object) {
       object = RILRequest.obtain(51, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getCDMASubscription(Message object) {
       object = RILRequest.obtain(95, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getCLIR(Message object) {
       object = RILRequest.obtain(31, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getCbConfig(Message object) {
       object = RILRequest.obtain(10008, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getCdmaBroadcastConfig(Message message) {
       this.send(RILRequest.obtain(92, message));
   }

   @Override
   public void getCdmaSubscriptionSource(Message object) {
       object = RILRequest.obtain(104, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getCellInfoList(Message object) {
       object = RILRequest.obtain(109, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getCurrentCalls(Message object) {
       object = RILRequest.obtain(9, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getDataCallList(Message object) {
       object = RILRequest.obtain(57, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getDataRegistrationState(Message object) {
       object = RILRequest.obtain(21, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getDeviceIdentity(Message object) {
       object = RILRequest.obtain(98, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getGsmBroadcastConfig(Message object) {
       object = RILRequest.obtain(89, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getHardwareConfig(Message object) {
       object = RILRequest.obtain(124, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getICBarring(String string, String string2, int n, Message object) {
       RILRequest rILRequest = RILRequest.obtain(10027, (Message)object);
       String string3 = rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest);
       object = string3;
       if (!SHIP_BUILD) {
           object = string3 + " [" + string + " " + n + "]";
       }
       this.riljLog((String)object);
       rILRequest.mParcel.writeInt(4);
       rILRequest.mParcel.writeString(string);
       rILRequest.mParcel.writeString(string2);
       rILRequest.mParcel.writeString(Integer.toString(n));
       rILRequest.mParcel.writeString("");
       this.send(rILRequest);
   }

   @Override
   public void getIMEI(Message object) {
       object = RILRequest.obtain(38, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getIMEISV(Message object) {
       object = RILRequest.obtain(39, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getIMSI(Message message) {
       this.getIMSIForApp(null, message);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void getIMSIForApp(String string, Message object) {
       object = RILRequest.obtain(11, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeString(string);
       String string2 = object.serialString() + "> getIMSI: " + RIL.requestToString(object.mRequest) + " aid: ";
       string = !SHIP_BUILD ? string2 + string : string2 + "xxx";
       this.riljLog(string);
       this.send((RILRequest)object);
   }

   @Override
   public void getIccCardStatus(Message object) {
       object = RILRequest.obtain(1, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getImsRegistrationState(Message object) {
       object = RILRequest.obtain(112, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getLastCallFailCause(Message object) {
       object = RILRequest.obtain(18, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getLastDataCallFailCause(Message object) {
       object = RILRequest.obtain(56, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Deprecated
   @Override
   public void getLastPdpFailCause(Message message) {
       this.getLastDataCallFailCause(message);
   }

   /*
    * Unable to fully structure code
    * Enabled aggressive block sorting
    * Enabled unnecessary exception pruning
    * Enabled aggressive exception aggregation
    * Lifted jumps to return sites
    */
   @Override
   public void getModemCapability(Message var1_1) {
       var3_5 = new ByteArrayOutputStream();
       var2_6 = new DataOutputStream(var3_5);
       Rlog.d((String)"RILJ", (String)"getModemCapability");
       var2_6.writeByte(2);
       var2_6.writeByte(97);
       var2_6.writeShort(4);
       var2_6.close();
       ** GOTO lbl24
       {
           catch (Exception var2_7) {}
       }
       catch (IOException var1_2) {
           try {
               var2_6.close();
               return;
           }
           catch (Exception var1_3) {
               return;
           }
       }
       catch (Throwable var1_4) {
           try {
               var2_6.close();
           }
           catch (Exception var2_8) {
               throw var1_4;
           }
           throw var1_4;
lbl24: // 2 sources:
           this.invokeOemRilRequestRaw(var3_5.toByteArray(), var1_1);
           return;
       }
   }

   @Override
   public void getMute(Message object) {
       object = RILRequest.obtain(54, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getNeighboringCids(Message object) {
       object = RILRequest.obtain(75, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getNetworkSelectionMode(Message object) {
       object = RILRequest.obtain(45, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getOperator(Message object) {
       object = RILRequest.obtain(22, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Deprecated
   @Override
   public void getPDPContextList(Message message) {
       this.getDataCallList(message);
   }

   @Override
   public void getPhoneBookEntry(int n, int n2, int n3, String string, Message object) {
       object = RILRequest.obtain(10010, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(n);
       object.mParcel.writeInt(n2);
       object.mParcel.writeString(null);
       object.mParcel.writeInt(n3);
       object.mParcel.writeInt(0);
       object.mParcel.writeInt(0);
       object.mParcel.writeString(null);
       object.mParcel.writeString(string);
       this.send((RILRequest)object);
   }

   @Override
   public void getPhoneBookStorageInfo(int n, Message object) {
       object = RILRequest.obtain(10009, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.send((RILRequest)object);
   }

   @Override
   public void getPreferredNetworkList(Message object) {
       object = RILRequest.obtain(10016, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getPreferredNetworkType(Message object) {
       object = RILRequest.obtain(74, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getPreferredVoicePrivacy(Message object) {
       object = RILRequest.obtain(83, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getRadioCapability(Message object) {
       object = RILRequest.obtain(130, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getSIMLockInfo(int n, int n2, Message object) {
       object = RILRequest.obtain(10013, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(n);
       object.mParcel.writeInt(n2);
       this.send((RILRequest)object);
   }

   @Override
   public void getSignalStrength(Message object) {
       object = RILRequest.obtain(19, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getSmscAddress(Message object) {
       object = RILRequest.obtain(100, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getUsimPBCapa(Message object) {
       object = RILRequest.obtain(10012, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getVoiceRadioTechnology(Message object) {
       object = RILRequest.obtain(108, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void getVoiceRegistrationState(Message object) {
       object = RILRequest.obtain(20, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void handleCallSetupRequestFromSim(boolean bl, Message object) {
       int n = 1;
       this.riljLog("handleCallSetupRequestFromSim");
       object = RILRequest.obtain(71, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       if (!bl) {
           n = 0;
       }
       object.mParcel.writeIntArray(new int[]{n});
       this.send((RILRequest)object);
   }

   @Override
   public void hangupConnection(int n, Message object) {
       this.riljLog("hangupConnection: gsmIndex=" + n);
       object = RILRequest.obtain(12, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + n);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.send((RILRequest)object);
   }

   @Override
   public void hangupForegroundResumeBackground(Message object) {
       object = RILRequest.obtain(14, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void hangupVT(int n, Message object) {
       object = RILRequest.obtain(10021, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " rejectCause: " + n);
       this.send((RILRequest)object);
   }

   @Override
   public void hangupWaitingOrBackground(Message object) {
       object = RILRequest.obtain(13, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void holdCall(Message object) {
       object = RILRequest.obtain(10022, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void iccCloseLogicalChannel(int n, Message object) {
       object = RILRequest.obtain(116, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void iccIO(int n, int n2, String string, int n3, int n4, int n5, String string2, String string3, Message message) {
       this.iccIOForApp(n, n2, string, n3, n4, n5, string2, string3, null, message);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void iccIOForApp(int n, int n2, String string, int n3, int n4, int n5, String string2, String string3, String string4, Message object) {
       object = RILRequest.obtain(28, (Message)object);
       object.mParcel.writeInt(n);
       object.mParcel.writeInt(n2);
       object.mParcel.writeString(string);
       object.mParcel.writeInt(n3);
       object.mParcel.writeInt(n4);
       object.mParcel.writeInt(n5);
       object.mParcel.writeString(string2);
       object.mParcel.writeString(string3);
       object.mParcel.writeString(string4);
       string = object.serialString() + "> iccIO: " + RIL.requestToString(object.mRequest) + " 0x" + Integer.toHexString(n) + " 0x" + Integer.toHexString(n2) + " " + " path: " + string + "," + n3 + "," + n4 + "," + n5 + " aid: ";
       string = !SHIP_BUILD ? string + string4 : string + "xxx";
       this.riljLog(string);
       this.send((RILRequest)object);
   }

   @Override
   public void iccOpenLogicalChannel(String string, Message object) {
       object = RILRequest.obtain(115, (Message)object);
       object.mParcel.writeString(string);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void iccTransmitApduBasicChannel(int n, int n2, int n3, int n4, int n5, String string, Message message) {
       this.iccTransmitApduHelper(114, 0, n, n2, n3, n4, n5, string, message);
   }

   @Override
   public void iccTransmitApduLogicalChannel(int n, int n2, int n3, int n4, int n5, int n6, String string, Message message) {
       if (n <= 0) {
           throw new RuntimeException("Invalid channel in iccTransmitApduLogicalChannel: " + n);
       }
       this.iccTransmitApduHelper(117, n, n2, n3, n4, n5, n6, string, message);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void invokeOemRilRequestRaw(byte[] arrby, Message object) {
       RILRequest rILRequest = RILRequest.obtain(59, (Message)object);
       String string = IccUtils.bytesToHexString(arrby);
       object = string;
       if (SHIP_BUILD) {
           if ("15".equals(string.substring(0, 2))) {
               object = "****";
           } else {
               object = string;
               if ("1627".equals(string.substring(0, 4))) {
                   object = string;
                   if ("KOREA".equals(SystemProperties.get((String)"ro.csc.country_code"))) {
                       object = "****";
                   }
               }
           }
       }
       this.riljLog(rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest) + "[" + (String)object + "]");
       rILRequest.mParcel.writeByteArray(arrby);
       this.send(rILRequest);
   }

   @Override
   public void invokeOemRilRequestStrings(String[] arrstring, Message object) {
       object = RILRequest.obtain(60, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeStringArray(arrstring);
       this.send((RILRequest)object);
   }

   @Override
   public void modifyCallConfirm(CallModify callModify, Message object) {
       object = RILRequest.obtain(10004, (Message)object);
       object.mParcel.writeInt(callModify.call_index);
       object.mParcel.writeInt(callModify.call_details.call_type);
       object.mParcel.writeInt(callModify.call_details.call_domain);
       object.mParcel.writeString(callModify.call_details.getCsvFromExtras());
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + callModify);
       this.send((RILRequest)object);
   }

   @Override
   public void modifyCallInitiate(CallModify callModify, Message object) {
       object = RILRequest.obtain(10003, (Message)object);
       object.mParcel.writeInt(callModify.call_index);
       object.mParcel.writeInt(callModify.call_details.call_type);
       object.mParcel.writeInt(callModify.call_details.call_domain);
       object.mParcel.writeString(callModify.call_details.getCsvFromExtras());
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + callModify);
       this.send((RILRequest)object);
   }

   /*
    * Unable to fully structure code
    * Enabled aggressive block sorting
    * Lifted jumps to return sites
    */
   public String modifyDnsByCpa(String var1_1, String var2_2) {
       block5 : {
           var6_3 = Settings.Secure.getInt((ContentResolver)this.mContext.getContentResolver(), (String)"kddi_cpa_on", (int)0) != 0;
           var4_4 = Settings.Secure.getString((ContentResolver)this.mContext.getContentResolver(), (String)"kddi_cpa_static_dns1");
           var5_5 = Settings.Secure.getString((ContentResolver)this.mContext.getContentResolver(), (String)"kddi_cpa_static_dns2");
           this.riljLog("modifyDnsByCpa (isKddiCpaOn:" + var6_3 + ", cpaDns1:" + var4_4 + "cpaDns2:" + var5_5 + ", dnses:" + (String)var1_1 + ")");
           if (var5_5 != null) {
               var3_6 = var5_5;
               if (!var5_5.equals("")) break block5;
           }
           var3_6 = "0.0.0.0";
       }
       if (var6_3 == false) return var1_1;
       var5_5 = "net." + (String)var2_2 + ".";
       var2_2 = new String[2];
       if (var4_4 == null) ** GOTO lbl16
       var2_2 = var4_4;
       if (!var4_4.equals("")) ** GOTO lbl19
lbl16: // 2 sources:
       if (TextUtils.isEmpty((CharSequence)var1_1)) {
           var2_2 = "1.0.0.0";
           var3_6 = "1.0.0.0";
lbl19: // 2 sources:
           SystemProperties.set((String)(var5_5 + "dns1"), (String)var2_2);
           SystemProperties.set((String)(var5_5 + "dns2"), (String)var3_6);
           return (String)var2_2 + " " + var3_6;
       }
       var1_1 = var1_1.split(" ");
       this.riljLog("tmpdns[0]:" + var1_1[0] + ", tmpDns[1]:" + var1_1[1]);
       if (var1_1[0].equals("0.0.0.0")) {
           var1_1[0] = "1.0.0.0";
       }
       if (var1_1[1].equals("0.0.0.0")) {
           var1_1[1] = "1.0.0.0";
       }
       SystemProperties.set((String)(var5_5 + "dns1"), (String)var1_1[0]);
       SystemProperties.set((String)(var5_5 + "dns2"), (String)var1_1[1]);
       return (String)var1_1[0] + " " + (String)var1_1[1];
   }

   /*
    * Unable to fully structure code
    * Enabled aggressive block sorting
    * Lifted jumps to return sites
    */
   public int modifyNetworkTypeByOperator(int var1_1) {
       SystemProperties.getBoolean((String)"persist.radio.dcmlte", (boolean)true);
       SystemProperties.getBoolean((String)"gsm.operator.isroaming", (boolean)false);
       var6_2 = SystemProperties.getInt((String)"persist.radio.setnwkmode", (int)9);
       var8_3 = Settings.Global.getInt((ContentResolver)this.mContext.getContentResolver(), (String)"mobile_data", (int)1) == 1;
       var9_4 = Settings.Global.getInt((ContentResolver)this.mContext.getContentResolver(), (String)"data_roaming", (int)1) == 1;
       if (Settings.System.getInt((ContentResolver)this.mContext.getContentResolver(), (String)"voicecall_type", (int)1) == 0) {
           // empty if block
       }
       var2_5 = SystemProperties.get((String)"gsm.operator.numeric", (String)"");
       var3_6 = SystemProperties.get((String)"gsm.sim.operator.numeric", (String)"44050");
       if ("00101".equals(var3_6) != false) return var1_1;
       if ("99999".equals(var3_6) != false) return var1_1;
       if ("45001".equals(var3_6)) {
           return var1_1;
       }
       var7_7 = var2_5.length() < 3 ? false : var3_6.substring(0, 3).equals(var2_5.substring(0, 3)) == false && "000".equals(var2_5.substring(0, 3)) == false && "000".equals(var3_6.substring(0, 3)) == false;
       this.riljLog("modifyNetworkTypeByOperator (preferredNetworkType:" + var6_2 + ", userDataEnabled:" + var8_3 + ", isRoaming:" + var7_7 + ", userDataRoamingEnabled:" + var9_4 + ")");
       var1_1 = var8_3 != false && (var7_7 == false || var9_4 != false) ? 1 : 0;
       var4_8 = var1_1;
       if ("KDI".equals("EUR")) {
           var8_3 = PreferenceManager.getDefaultSharedPreferences((Context)this.mContext).getBoolean("japan_system_select_key", true);
           this.riljLog("... LteDataComm:" + var8_3);
           var4_8 = var7_7 ? (var1_1 != 0 && var8_3 ? 1 : 0) : 1;
       }
       var1_1 = var5_9 = var6_2;
       if (var4_8 != 0) return var1_1;
       var1_1 = var5_9;
       switch (var6_2) {
           default: {
               var1_1 = var5_9;
               ** GOTO lbl37
           }
           case 12: {
               var1_1 = 2;
               ** GOTO lbl37
           }
           case 9: {
               var1_1 = 3;
               ** GOTO lbl37
           }
           case 8: {
               var1_1 = 4;
           }
lbl37: // 5 sources:
           case 11: {
               return var1_1;
           }
           case 10:
       }
       return 7;
   }

   protected void notifyModemCap(byte[] asyncResult, Integer n) {
       asyncResult = new AsyncResult((Object)null, (Object)new UnsolOemHookBuffer(n, (byte[])asyncResult), null);
       this.mModemCapRegistrants.notifyRegistrants(asyncResult);
       Rlog.d((String)"RILJ", (String)("MODEM_CAPABILITY on phone=" + n + " notified to registrants"));
   }

   @Override
   public void nvReadItem(int n, Message object) {
       object = RILRequest.obtain(118, (Message)object);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + ' ' + n);
       this.send((RILRequest)object);
   }

   @Override
   public void nvResetConfig(int n, Message object) {
       object = RILRequest.obtain(121, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + ' ' + n);
       this.send((RILRequest)object);
   }

   @Override
   public void nvWriteCdmaPrl(byte[] arrby, Message object) {
       object = RILRequest.obtain(120, (Message)object);
       object.mParcel.writeByteArray(arrby);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " (" + arrby.length + " bytes)");
       this.send((RILRequest)object);
   }

   @Override
   public void nvWriteItem(int n, String string, Message object) {
       object = RILRequest.obtain(119, (Message)object);
       object.mParcel.writeInt(n);
       object.mParcel.writeString(string);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + ' ' + n + ": " + string);
       this.send((RILRequest)object);
   }

   @Override
   protected void onRadioAvailable() {
       this.updateScreenState();
   }

   /*
    * Exception decompiling
    */
   protected RILRequest processSolicited(Parcel var1_1) {
       // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
       // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
       // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:143)
       // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:385)
       // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
       // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
       // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
       // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
       // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
       // org.benf.cfr.reader.entities.Method.analyse(Method.java:355)
       // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:768)
       // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:700)
       // org.benf.cfr.reader.Main.doJar(Main.java:134)
       // org.benf.cfr.reader.Main.main(Main.java:189)
       throw new IllegalStateException("Decompilation failed");
   }

   /*
    * Unable to fully structure code
    * Enabled aggressive block sorting
    * Enabled unnecessary exception pruning
    * Enabled aggressive exception aggregation
    * Lifted jumps to return sites
    */
   protected void processUnsolicited(Parcel var1_1) {
       var4_5 = var1_1.readInt();
       switch (var4_5) {
           default: {
               var1_1 = new Object();
               var2_6 = new RuntimeException(var1_1.append("Unrecognized unsol response: ").append(var4_5).toString());
               throw var2_6;
           }
           case 1000: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 1001: {
               try {
                   this.mDtmfProgress = 0;
                   var2_7 = this.responseVoid((Parcel)var1_1);
                   break;
               }
               catch (Throwable var1_2) {
                   Rlog.e((String)"RILJ", (String)("Exception processing unsol response: " + var4_5 + "Exception:" + var1_2.toString()));
                   return;
               }
           }
           case 1002: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 1003: {
               var2_7 = this.responseString((Parcel)var1_1);
               break;
           }
           case 1004: {
               var2_7 = this.responseString((Parcel)var1_1);
               break;
           }
           case 1005: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 1006: {
               var2_7 = this.responseStrings((Parcel)var1_1);
               break;
           }
           case 1008: {
               var2_7 = this.responseString((Parcel)var1_1);
               break;
           }
           case 1009: {
               var2_7 = this.responseSignalStrength((Parcel)var1_1);
               break;
           }
           case 1010: {
               var2_7 = this.responseDataCallList((Parcel)var1_1);
               break;
           }
           case 1011: {
               var2_7 = this.responseSuppServiceNotification((Parcel)var1_1);
               break;
           }
           case 1012: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 1013: {
               var2_7 = this.responseString((Parcel)var1_1);
               break;
           }
           case 1014: {
               var2_7 = this.responseString((Parcel)var1_1);
               break;
           }
           case 1015: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 1016: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 1017: {
               var2_7 = this.responseSimRefresh((Parcel)var1_1);
               break;
           }
           case 1018: {
               var2_7 = this.responseCallRing((Parcel)var1_1);
               break;
           }
           case 1023: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 1019: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 1020: {
               var2_7 = this.responseCdmaSms((Parcel)var1_1);
               break;
           }
           case 1021: {
               var2_7 = this.responseRaw((Parcel)var1_1);
               break;
           }
           case 1022: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 1024: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 1025: {
               var2_7 = this.responseCdmaCallWaiting((Parcel)var1_1);
               break;
           }
           case 1026: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 1027: {
               var2_7 = this.responseCdmaInformationRecord((Parcel)var1_1);
               break;
           }
           case 1028: {
               var2_7 = this.responseRaw((Parcel)var1_1);
               break;
           }
           case 1029: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 1030: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 1031: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 1032: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 1033: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 1034: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 1035: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 1036: {
               var2_7 = this.responseCellInfoList((Parcel)var1_1);
               break;
           }
           case 1037: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 1038: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 1039: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 1040: {
               var2_7 = this.responseHardwareConfig((Parcel)var1_1);
               break;
           }
           case 1042: {
               var2_7 = this.responseRadioCapability((Parcel)var1_1);
               break;
           }
           case 1043: {
               var2_7 = this.responseSsData((Parcel)var1_1);
               break;
           }
           case 1044: {
               var2_7 = this.responseString((Parcel)var1_1);
               break;
           }
           case 11001: {
               var2_7 = this.responseSSReleaseCompleteNotification((Parcel)var1_1);
               break;
           }
           case 11002: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 11003: {
               var2_7 = this.responseString((Parcel)var1_1);
               break;
           }
           case 11008: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 11009: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 11010: {
               var2_7 = this.responseString((Parcel)var1_1);
               break;
           }
           case 11013: {
               var2_7 = this.responseRaw((Parcel)var1_1);
               break;
           }
           case 11020: {
               var2_7 = this.responseRaw((Parcel)var1_1);
               break;
           }
           case 11021: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 11024: {
               var2_7 = this.responseRaw((Parcel)var1_1);
               break;
           }
           case 11027: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 11028: {
               var2_7 = this.responseCallModify((Parcel)var1_1);
               break;
           }
           case 11030: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 11032: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 11034: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 11035: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 11037: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 11043: {
               var2_7 = this.responseVoid((Parcel)var1_1);
               break;
           }
           case 11054: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 11056: {
               var2_7 = this.responseRaw((Parcel)var1_1);
               break;
           }
           case 11060: {
               var2_7 = this.responseStrings((Parcel)var1_1);
               break;
           }
           case 11061: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 11062: {
               var2_7 = this.responseInts((Parcel)var1_1);
               break;
           }
           case 11066: {
               var2_7 = this.responseString((Parcel)var1_1);
           }
       }
       switch (var4_5) {
           default: {
               return;
           }
           case 1000: {
               var1_1 = this.getRadioStateFromInt(var1_1.readInt());
               this.unsljLogMore(var4_5, var1_1.toString());
               this.switchToRadioState((CommandsInterface.RadioState)var1_1);
               return;
           }
           case 1037: {
               this.unsljLog(var4_5);
               this.mImsNetworkStateChangedRegistrants.notifyRegistrants(new AsyncResult((Object)null, (Object)null, null));
               return;
           }
           case 1001: {
               this.unsljLog(var4_5);
               this.mCallStateRegistrants.notifyRegistrants(new AsyncResult((Object)null, (Object)null, null));
               return;
           }
           case 1002: {
               this.unsljLog(var4_5);
               this.mVoiceNetworkStateRegistrants.notifyRegistrants(new AsyncResult((Object)null, (Object)null, null));
               return;
           }
           case 1003: {
               this.unsljLog(var4_5);
               var1_1 = new String[2];
               var1_1[1] = (String)var2_7;
               var1_1 = SmsMessage.newFromCMT((String[])var1_1);
               if (this.mGsmSmsRegistrant != null) {
                   this.mGsmSmsRegistrant.notifyRegistrant(new AsyncResult((Object)null, var1_1, null));
               }
               if (SystemProperties.getBoolean((String)"persist.EarthquakeTestmode", (boolean)false) == false) return;
               this.testingETWS((SmsMessage)var1_1);
               return;
           }
           case 1004: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mSmsStatusRegistrant == null) return;
               this.mSmsStatusRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1005: {
               this.unsljLogRet(var4_5, var2_7);
               var1_1 = (int[])var2_7;
               if (var1_1.length == 1) {
                   if (this.mSmsOnSimRegistrant == null) return;
                   this.mSmsOnSimRegistrant.notifyRegistrant(new AsyncResult((Object)null, var1_1, null));
                   return;
               }
               this.riljLog(" NEW_SMS_ON_SIM ERROR with wrong length " + var1_1.length);
               return;
           }
           case 1006: {
               var3_8 = (String[])var2_7;
               var1_1 = var3_8;
               if (var3_8.length < 2) {
                   var1_1 = new String[]{((String[])var2_7)[0], null};
               }
               this.unsljLogMore(var4_5, (String)var1_1[0]);
               if (this.mUSSDRegistrant == null) return;
               this.mUSSDRegistrant.notifyRegistrant(new AsyncResult((Object)null, var1_1, null));
               return;
           }
           case 1008: {
               this.unsljLogRet(var4_5, var2_7);
               var5_10 = var1_1.readLong();
               var1_1 = new Object[]{var2_7, var5_10};
               if (SystemProperties.getBoolean((String)"telephony.test.ignore.nitz", (boolean)false)) {
                   this.riljLog("ignoring UNSOL_NITZ_TIME_RECEIVED");
                   return;
               }
               if (this.mNITZTimeRegistrant != null) {
                   this.mNITZTimeRegistrant.notifyRegistrant(new AsyncResult((Object)null, var1_1, null));
               }
               this.mLastNITZTimeInfo = var1_1;
               return;
           }
           case 1009: {
               if (this.mSignalStrengthRegistrant == null) return;
               this.mSignalStrengthRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1010: {
               this.unsljLogRet(var4_5, var2_7);
               this.mDataNetworkStateRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1011: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mSsnRegistrant == null) return;
               this.mSsnRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1012: {
               this.unsljLog(var4_5);
               if (this.mCatSessionEndRegistrant == null) return;
               this.mCatSessionEndRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1013: {
               this.unsljLog(var4_5);
               if (this.mCatProCmdRegistrant == null) return;
               this.mCatProCmdRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1014: {
               this.unsljLog(var4_5);
               if (this.mCatEventRegistrant == null) return;
               this.mCatEventRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1015: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mCatCallSetUpRegistrant == null) return;
               this.mCatCallSetUpRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1016: {
               this.unsljLog(var4_5);
               if (this.mIccSmsFullRegistrant == null) return;
               this.mIccSmsFullRegistrant.notifyRegistrant();
               return;
           }
           case 1017: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mIccRefreshRegistrants == null) return;
               this.mIccRefreshRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1018: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mRingRegistrant == null) return;
               this.mRingRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1023: {
               this.unsljLogvRet(var4_5, var2_7);
               if (this.mRestrictedStateRegistrant == null) return;
               this.mRestrictedStateRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1019: {
               this.unsljLog(var4_5);
               if (this.mIccStatusChangedRegistrants == null) return;
               this.mIccStatusChangedRegistrants.notifyRegistrants();
               return;
           }
           case 1020: {
               this.unsljLog(var4_5);
               var1_1 = (SmsMessage)var2_7;
               if (this.mCdmaSmsRegistrant == null) return;
               this.mCdmaSmsRegistrant.notifyRegistrant(new AsyncResult((Object)null, var1_1, null));
               return;
           }
           case 1021: {
               this.unsljLogvRet(var4_5, IccUtils.bytesToHexString((byte[])var2_7));
               if (this.mGsmBroadcastSmsRegistrant == null) return;
               this.mGsmBroadcastSmsRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1022: {
               this.unsljLog(var4_5);
               if (this.mIccSmsFullRegistrant == null) return;
               this.mIccSmsFullRegistrant.notifyRegistrant();
               return;
           }
           case 1024: {
               this.unsljLog(var4_5);
               if (this.mEmergencyCallbackModeRegistrant == null) return;
               this.mEmergencyCallbackModeRegistrant.notifyRegistrant();
               return;
           }
           case 1025: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mCallWaitingInfoRegistrants == null) return;
               this.mCallWaitingInfoRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1026: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mOtaProvisionRegistrants == null) return;
               this.mOtaProvisionRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1027: {
               try {
                   var1_1 = (ArrayList)var2_7;
               }
               catch (ClassCastException var1_3) {
                   Rlog.e((String)"RILJ", (String)"Unexpected exception casting to listInfoRecs", (Throwable)var1_3);
                   return;
               }
               var1_1 = var1_1.iterator();
               while (var1_1.hasNext() != false) {
                   var2_7 = (CdmaInformationRecords)var1_1.next();
                   this.unsljLogRet(var4_5, var2_7);
                   this.notifyRegistrantsCdmaInfoRec((CdmaInformationRecords)var2_7);
               }
               return;
           }
           case 1028: {
               this.unsljLogvRet(var4_5, IccUtils.bytesToHexString((byte[])var2_7));
               if (this.mUnsolOemHookRawRegistrant == null) return;
               this.mUnsolOemHookRawRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1029: {
               this.unsljLogvRet(var4_5, var2_7);
               if (this.mRingbackToneRegistrants == null) return;
               var7_11 = ((int[])var2_7)[0] == 1;
               this.mRingbackToneRegistrants.notifyRegistrants(new AsyncResult((Object)null, (Object)var7_11, null));
               return;
           }
           case 1030: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mResendIncallMuteRegistrants == null) return;
               this.mResendIncallMuteRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1035: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mVoiceRadioTechChangedRegistrants == null) return;
               this.mVoiceRadioTechChangedRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1031: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mCdmaSubscriptionChangedRegistrants == null) return;
               this.mCdmaSubscriptionChangedRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1032: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mCdmaPrlChangedRegistrants == null) return;
               this.mCdmaPrlChangedRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1033: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mExitEmergencyCallbackModeRegistrants == null) return;
               this.mExitEmergencyCallbackModeRegistrants.notifyRegistrants(new AsyncResult((Object)null, (Object)null, null));
               return;
           }
           case 1034: {
               Rlog.i((String)"RILJ", (String)"!@Boot_SVC : RIL_UNSOL_RIL_CONNECTED");
               this.unsljLogRet(var4_5, var2_7);
               this.getRadioCapability(this.mSupportedRafHandler.obtainMessage());
               if (!"DCGG".equals("")) ** GOTO lbl423
               Rlog.d((String)"RILJ", (String)"do not power off radio");
               ** GOTO lbl437
lbl423: // 1 sources:
               if (!"DGG".equals("") || this.mInstanceId != 1) ** GOTO lbl426
               Rlog.d((String)"RILJ", (String)"do not power off radio");
               ** GOTO lbl437
lbl426: // 1 sources:
               if (!"DCGGS".equals("")) ** GOTO lbl433
               var3_9 = SystemProperties.get((String)"ril.rildreset", (String)"");
               if ("8".equals(var3_9)) {
                   var1_1 = SystemProperties.get((String)"ril.RildInit", (String)"");
                   var4_5 = 1;
                   break;
               }
               ** GOTO lbl589
lbl433: // 1 sources:
               if (TelephonyManager.getDefault().getPhoneCount() > 1 && this.mInstanceId == 1) {
                   Rlog.d((String)"RILJ", (String)"Do not power off radio for slot2");
               } else {
                   this.setRadioPower(false, null);
               }
lbl437: // 6 sources:
               do {
                   this.setCellInfoListRate(Integer.MAX_VALUE, null);
                   this.notifyRegistrantsRilConnectionChanged(((int[])var2_7)[0]);
                   return;
                   break;
               } while (true);
           }
           case 1036: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mRilCellInfoListRegistrants == null) return;
               this.mRilCellInfoListRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1038: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mSubscriptionStatusRegistrants == null) return;
               this.mSubscriptionStatusRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1039: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mSrvccStateRegistrants == null) return;
               this.mSrvccStateRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1040: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mHardwareConfigChangeRegistrants == null) return;
               this.mHardwareConfigChangeRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1042: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mPhoneRadioCapabilityChangedRegistrants == null) return;
               this.mPhoneRadioCapabilityChangedRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1043: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mSsRegistrant == null) return;
               this.mSsRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 1044: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mCatCcAlphaRegistrant == null) return;
               this.mCatCcAlphaRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11010: {
               var1_1 = (String)var2_7;
               Rlog.d((String)"RILJ", (String)("Executing Am " + (String)var1_1));
               Am.main(var1_1.split(" "));
               return;
           }
           case 11027: {
               this.unsljLogvRet(var4_5, var2_7);
               if (this.mImsRegistrationStateChangedRegistrants == null) return;
               this.mImsRegistrationStateChangedRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11028: {
               this.unsljLogvRet(var4_5, var2_7);
               if (this.mModifyCallRegistrants == null) return;
               this.mModifyCallRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11002: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mCatSendSmsResultRegistrant == null) return;
               this.mCatSendSmsResultRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11003: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mCatCallControlResultRegistrant == null) return;
               this.mCatCallControlResultRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11001: {
               this.unsljLog(var4_5);
               if (this.mReleaseCompleteMessageRegistrant == null) return;
               this.mReleaseCompleteMessageRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11035: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mPbInitCompleteRegistrant == null) return;
               this.mPbInitCompleteRegistrant.notifyRegistrant(new AsyncResult((Object)null, (Object)null, null));
               return;
           }
           case 11021: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mSimPbReadyRegistrant == null) return;
               this.mSimPbReadyRegistrant.notifyRegistrant(new AsyncResult((Object)null, (Object)null, null));
               return;
           }
           case 11037: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mHysteresisDCNRegistant == null) return;
               this.mHysteresisDCNRegistant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11008: {
               this.unsljLog(var4_5);
               if (this.mSmsDeviceReadyRegistrant == null) return;
               this.mSmsDeviceReadyRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11043: {
               this.unsljLog(var4_5);
               if (this.mHomeNetworkRegistant == null) return;
               this.mHomeNetworkRegistant.notifyRegistrant();
               return;
           }
           case 11030: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mCsFallbackRegistant == null) return;
               this.mCsFallbackRegistant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11032: {
               this.unsljLogvRet(var4_5, var2_7);
               if (this.mVoiceSystemIdRegistrant == null) return;
               this.mVoiceSystemIdRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11054: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mStkSetupCallStatus == null) return;
               this.mStkSetupCallStatus.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11061: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mImsPrefNotiRegistrant == null) return;
               this.mImsPrefNotiRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11056: {
               this.unsljLogRet(var4_5, var2_7);
               Rlog.d((String)"RILJ", (String)("UNSOL_MODEM_CAPABILITY = mInstanceId" + this.mInstanceId));
               this.notifyModemCap((byte[])var2_7, this.mInstanceId);
               return;
           }
           case 11060: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mDunStateChangedRegistrant == null) return;
               return;
           }
           case 11034: {
               this.unsljLog(var4_5);
               Rlog.d((String)"RILJ", (String)"RIL_UNSOL_IMS_RETRYOVER");
               if (this.mImsRegistrationRetryOver == null) return;
               this.mImsRegistrationRetryOver.notifyRegistrants(new AsyncResult((Object)null, (Object)null, null));
               return;
           }
           case 11013: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mSapRegistant == null) return;
               this.mSapRegistant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11062: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mIccAppRefreshRegistrant == null) return;
               this.mIccAppRefreshRegistrant.notifyRegistrant(new AsyncResult((Object)null, var2_7, null));
               return;
           }
           case 11066: {
               this.unsljLogRet(var4_5, var2_7);
               if (this.mSimIccIdNotiRegistrants == null) return;
               this.mSimIccIdNotiRegistrants.notifyRegistrants(new AsyncResult((Object)null, var2_7, null));
               return;
           }
       }
       do {
           block155 : {
               if (!"1".equals(var1_1) && var4_5 < 8) {
                   Rlog.d((String)"RILJ", (String)("Rild is not ready, reTry " + var4_5 + "times"));
                   Thread.sleep(500);
                   break block155;
               }
               Rlog.d((String)"RILJ", (String)"[CGG] Notify ril connected event to CP!");
               this.setSimPower(9, null);
lbl589: // 2 sources:
               if (!"8".equals(var3_9) && !"9".equals(var3_9)) ** GOTO lbl437
               Rlog.d((String)"RILJ", (String)"[CGG] rildreset property value set as zero!");
               SystemProperties.set((String)"ril.rildreset", (String)"0");
               ** continue;
               catch (InterruptedException var1_4) {}
           }
           var1_1 = SystemProperties.get((String)"ril.RildInit", (String)"");
           ++var4_5;
       } while (true);
   }

   @Override
   public void queryAvailableBandMode(Message object) {
       object = RILRequest.obtain(66, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void queryCLIP(Message object) {
       object = RILRequest.obtain(55, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void queryCNAP(Message object) {
       object = RILRequest.obtain(10029, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void queryCallForwardStatus(int n, int n2, String string, Message object) {
       object = RILRequest.obtain(33, (Message)object);
       object.mParcel.writeInt(2);
       object.mParcel.writeInt(n);
       object.mParcel.writeInt(n2);
       object.mParcel.writeInt(PhoneNumberUtils.toaFromString((String)string));
       object.mParcel.writeString(string);
       object.mParcel.writeInt(0);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + n + " " + n2);
       this.send((RILRequest)object);
   }

   @Override
   public void queryCallWaiting(int n, Message object) {
       object = RILRequest.obtain(35, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + n);
       this.send((RILRequest)object);
   }

   @Override
   public void queryCdmaRoamingPreference(Message object) {
       object = RILRequest.obtain(79, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void queryFacilityLock(String string, String string2, int n, Message message) {
       this.queryFacilityLockForApp(string, string2, n, null, message);
   }

   @Override
   public void queryFacilityLockForApp(String string, String string2, int n, String string3, Message object) {
       RILRequest rILRequest = RILRequest.obtain(42, (Message)object);
       String string4 = rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest);
       object = string4;
       if (!SHIP_BUILD) {
           object = string4 + " [" + string + " " + n + " " + string3 + "]";
       }
       this.riljLog((String)object);
       rILRequest.mParcel.writeInt(4);
       rILRequest.mParcel.writeString(string);
       rILRequest.mParcel.writeString(string2);
       rILRequest.mParcel.writeString(Integer.toString(n));
       rILRequest.mParcel.writeString(string3);
       this.send(rILRequest);
   }

   @Override
   public void queryTTYMode(Message object) {
       object = RILRequest.obtain(81, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void rejectCall(Message object) {
       object = RILRequest.obtain(17, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void reportSmsMemoryStatus(boolean bl, Message object) {
       int n = 1;
       object = RILRequest.obtain(102, (Message)object);
       object.mParcel.writeInt(1);
       Parcel parcel = object.mParcel;
       if (!bl) {
           n = 0;
       }
       parcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + ": " + bl);
       this.send((RILRequest)object);
   }

   @Override
   public void reportStkServiceIsRunning(Message object) {
       object = RILRequest.obtain(103, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void requestIccSimAuthentication(int n, String string, String string2, Message object) {
       object = RILRequest.obtain(125, (Message)object);
       object.mParcel.writeInt(n);
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void requestIsimAuthentication(String string, Message object) {
       object = RILRequest.obtain(105, (Message)object);
       object.mParcel.writeString(string);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void requestShutdown(Message object) {
       object = RILRequest.obtain(129, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void resetRadio(Message object) {
       object = RILRequest.obtain(58, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   /*
    * Enabled aggressive block sorting
    */
   protected Object responseCallList(Parcel parcel) {
       int n = parcel.readInt();
       ArrayList<DriverCall> arrayList = new ArrayList<DriverCall>(n);
       for (int i = 0; i < n; ++i) {
           DriverCall driverCall = new DriverCall();
           driverCall.state = DriverCall.stateFromCLCC(parcel.readInt());
           driverCall.index = parcel.readInt();
           driverCall.id = driverCall.index >> 8 & 255;
           driverCall.index &= 255;
           driverCall.TOA = parcel.readInt();
           boolean bl = parcel.readInt() != 0;
           driverCall.isMpty = bl;
           bl = parcel.readInt() != 0;
           driverCall.isMT = bl;
           driverCall.als = parcel.readInt();
           bl = parcel.readInt() != 0;
           driverCall.isVoice = bl;
           int n2 = parcel.readInt();
           int n3 = parcel.readInt();
           byte[] arrby = parcel.readString();
           driverCall.callDetails = new CallDetails(n2, n3, null);
           driverCall.callDetails.setExtrasFromCsv((String)arrby);
           Rlog.d((String)"RILJ", (String)("dc.index " + driverCall.index + " dc.id " + driverCall.id + " dc.callDetails " + driverCall.callDetails));
           bl = parcel.readInt() != 0;
           driverCall.isVoicePrivacy = bl;
           driverCall.number = parcel.readString();
           driverCall.numberPresentation = DriverCall.presentationFromCLIP(parcel.readInt());
           driverCall.name = parcel.readString();
           Rlog.d((String)"RILJ", (String)("responseCallList dc.name" + driverCall.name));
           driverCall.namePresentation = DriverCall.presentationFromCLIP(parcel.readInt());
           if (parcel.readInt() == 1) {
               driverCall.uusInfo = new UUSInfo();
               driverCall.uusInfo.setType(parcel.readInt());
               driverCall.uusInfo.setDcs(parcel.readInt());
               arrby = parcel.createByteArray();
               driverCall.uusInfo.setUserData(arrby);
               this.riljLogv(String.format("Incoming UUS : type=%d, dcs=%d, length=%d", driverCall.uusInfo.getType(), driverCall.uusInfo.getDcs(), driverCall.uusInfo.getUserData().length));
               this.riljLogv("Incoming UUS : data (string)=" + new String(driverCall.uusInfo.getUserData()));
               this.riljLogv("Incoming UUS : data (hex): " + IccUtils.bytesToHexString(driverCall.uusInfo.getUserData()));
           } else {
               this.riljLogv("Incoming UUS : NOT present!");
           }
           driverCall.number = PhoneNumberUtils.stringFromStringAndTOA((String)driverCall.number, (int)driverCall.TOA);
           arrayList.add(driverCall);
           if (driverCall.isVoicePrivacy) {
               this.mVoicePrivacyOnRegistrants.notifyRegistrants();
               this.riljLog("InCall VoicePrivacy is enabled");
               continue;
           }
           this.mVoicePrivacyOffRegistrants.notifyRegistrants();
           this.riljLog("InCall VoicePrivacy is disabled");
       }
       Collections.sort(arrayList);
       if (n == 0 && this.mTestingEmergencyCall.getAndSet(false) && this.mEmergencyCallbackModeRegistrant != null) {
           this.riljLog("responseCallList: call ended, testing emergency call, notify ECM Registrants");
           this.mEmergencyCallbackModeRegistrant.notifyRegistrant();
       }
       return arrayList;
   }

   protected Object responseCdmaCallWaiting(Parcel parcel) {
       CdmaCallWaitingNotification cdmaCallWaitingNotification = new CdmaCallWaitingNotification();
       cdmaCallWaitingNotification.number = parcel.readString();
       cdmaCallWaitingNotification.numberPresentation = CdmaCallWaitingNotification.presentationFromCLIP(parcel.readInt());
       cdmaCallWaitingNotification.name = parcel.readString();
       cdmaCallWaitingNotification.namePresentation = cdmaCallWaitingNotification.numberPresentation;
       cdmaCallWaitingNotification.isPresent = parcel.readInt();
       cdmaCallWaitingNotification.signalType = parcel.readInt();
       cdmaCallWaitingNotification.alertPitch = parcel.readInt();
       cdmaCallWaitingNotification.signal = parcel.readInt();
       cdmaCallWaitingNotification.numberType = parcel.readInt();
       cdmaCallWaitingNotification.numberPlan = parcel.readInt();
       return cdmaCallWaitingNotification;
   }

   /*
    * Enabled force condition propagation
    * Lifted jumps to return sites
    */
   protected Object responseSetupDataCall(Parcel object) {
       int n = object.readInt();
       int n2 = object.readInt();
       if (n < 5) {
           DataCallResponse dataCallResponse = new DataCallResponse();
           dataCallResponse.version = n;
           dataCallResponse.cid = Integer.parseInt(object.readString());
           dataCallResponse.ifname = object.readString();
           if (TextUtils.isEmpty((CharSequence)dataCallResponse.ifname)) {
               throw new RuntimeException("RIL_REQUEST_SETUP_DATA_CALL response, no ifname");
           }
           Object object2 = object.readString();
           if (!TextUtils.isEmpty((CharSequence)object2)) {
               dataCallResponse.addresses = object2.split(" ");
           }
           if (n2 >= 4) {
               object2 = object.readString();
               this.riljLog("responseSetupDataCall got dnses=" + (String)object2);
               if (!TextUtils.isEmpty((CharSequence)object2)) {
                   dataCallResponse.dnses = object2.split(" ");
               }
           }
           if (n2 >= 5) {
               object2 = object.readString();
               this.riljLog("responseSetupDataCall got gateways=" + (String)object2);
               if (!TextUtils.isEmpty((CharSequence)object2)) {
                   dataCallResponse.gateways = object2.split(" ");
               }
           }
           object2 = dataCallResponse;
           if (n2 < 6) return object2;
           object = object.readString();
           this.riljLog("responseSetupDataCall got pcscf=" + (String)object);
           object2 = dataCallResponse;
           if (TextUtils.isEmpty((CharSequence)object)) return object2;
           dataCallResponse.pcscf = object.split(" ");
           return dataCallResponse;
       }
       if (n2 == 1) return this.getDataCallResponse((Parcel)object, n);
       throw new RuntimeException("RIL_REQUEST_SETUP_DATA_CALL response expecting 1 RIL_Data_Call_response_v5 got " + n2);
   }

   /*
    * Enabled force condition propagation
    * Lifted jumps to return sites
    */
   protected void send(RILRequest rILRequest) {
       if (this.mSocket == null) {
           rILRequest.onError(1, null);
           rILRequest.release();
           do {
               return;
               break;
           } while (true);
       }
       rILRequest = this.mSender.obtainMessage(1, (Object)rILRequest);
       this.acquireWakeLock();
       rILRequest.sendToTarget();
   }

   @Override
   public void sendBurstDtmf(String string, int n, int n2, Message object) {
       object = RILRequest.obtain(85, (Message)object);
       object.mParcel.writeInt(3);
       object.mParcel.writeString(string);
       object.mParcel.writeString(Integer.toString(n));
       object.mParcel.writeString(Integer.toString(n2));
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " : " + string);
       this.send((RILRequest)object);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void sendCDMAFeatureCode(String string, Message object) {
       object = RILRequest.obtain(84, (Message)object);
       object.mParcel.writeString(string);
       if (!SHIP_BUILD) {
           this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " : " + string);
           this.send((RILRequest)object);
           return;
       }
       if (string == null || TextUtils.isEmpty((CharSequence)string)) {
           this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       } else {
           this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " : ...(" + string.length() + ")");
       }
       this.send((RILRequest)object);
   }

   @Override
   public void sendCdmaSms(byte[] arrby, Message object) {
       object = RILRequest.obtain(87, (Message)object);
       this.constructCdmaSendSmsRilRequest((RILRequest)object, arrby);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void sendCdmaSmsMore(byte[] arrby, Message object) {
       object = RILRequest.obtain(10020, (Message)object);
       this.constructCdmaSendSmsRilRequest((RILRequest)object, arrby);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void sendDtmf(char c, Message object) {
       object = RILRequest.obtain(24, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeString(Character.toString(c));
       this.send((RILRequest)object);
   }

   @Override
   public void sendEncodedUSSD(byte[] arrby, int n, int n2, Message object) {
       object = RILRequest.obtain(10019, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + IccUtils.bytesToHexString(arrby) + ", DCS : " + n2);
       object.mParcel.writeByteArray(arrby);
       object.mParcel.writeInt(n);
       object.mParcel.writeInt(n2);
       this.send((RILRequest)object);
   }

   @Override
   public void sendEnvelope(String string, Message object) {
       object = RILRequest.obtain(69, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeString(string);
       this.send((RILRequest)object);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void sendEnvelopeWithStatus(String string, Message object) {
       object = RILRequest.obtain(107, (Message)object);
       if (SHIP_BUILD) {
           this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       } else {
           this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + '[' + string + ']');
       }
       object.mParcel.writeString(string);
       this.send((RILRequest)object);
   }

   @Override
   public void sendImsCdmaSms(byte[] arrby, int n, int n2, Message object) {
       object = RILRequest.obtain(113, (Message)object);
       object.mParcel.writeInt(2);
       object.mParcel.writeByte((byte)n);
       object.mParcel.writeInt(n2);
       this.constructCdmaSendSmsRilRequest((RILRequest)object, arrby);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void sendImsGsmSms(String string, String string2, int n, int n2, Message object) {
       object = RILRequest.obtain(113, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeByte((byte)n);
       object.mParcel.writeInt(n2);
       this.constructGsmSendSmsRilRequest((RILRequest)object, string, string2);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void sendSMS(String string, String string2, Message object) {
       object = RILRequest.obtain(25, (Message)object);
       this.constructGsmSendSmsRilRequest((RILRequest)object, string, string2);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void sendSMSExpectMore(String string, String string2, Message object) {
       object = RILRequest.obtain(26, (Message)object);
       this.constructGsmSendSmsRilRequest((RILRequest)object, string, string2);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void sendSMSmore(String string, String string2, Message object) {
       object = RILRequest.obtain(26, (Message)object);
       Rlog.d((String)"RILJ", (String)("smscPDU: " + string));
       Rlog.d((String)"RILJ", (String)("pdu: " + string2));
       this.constructGsmSendSmsRilRequest((RILRequest)object, string, string2);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void sendTerminalResponse(String string, Message object) {
       object = RILRequest.obtain(70, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeString(string);
       this.send((RILRequest)object);
   }

   @Override
   public void sendUSSD(String string, Message object) {
       object = RILRequest.obtain(29, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + "*******");
       object.mParcel.writeString(string);
       this.send((RILRequest)object);
   }

   @Override
   public void separateConnection(int n, Message object) {
       object = RILRequest.obtain(52, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + n);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.send((RILRequest)object);
   }

   @Override
   public void setBandMode(int n, Message object) {
       object = RILRequest.obtain(65, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + n);
       this.send((RILRequest)object);
   }

   @Override
   public void setCLIR(int n, Message object) {
       object = RILRequest.obtain(32, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + n);
       this.send((RILRequest)object);
   }

   @Override
   public void setCallForward(int n, int n2, int n3, String string, int n4, Message message) {
       this.setCallForward(n, n2, n3, string, n4, null, null, message);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void setCallForward(int n, int n2, int n3, String object, int n4, String object2, String string, Message object3) {
       object3 = RILRequest.obtain(34, (Message)object3);
       object3.mParcel.writeInt(n);
       object3.mParcel.writeInt(n2);
       object3.mParcel.writeInt(n3);
       object3.mParcel.writeInt(PhoneNumberUtils.toaFromString((String)object));
       object3.mParcel.writeString((String)object);
       object3.mParcel.writeInt(n4);
       object = object2 != null && object2.contains(":") ? ((object = object2.split(":")).length == 2 ? String.format("%02d:%02d", Integer.parseInt(object[0]), Integer.parseInt(object[1])) : null) : null;
       object2 = string != null && string.contains(":") ? ((object2 = string.split(":")).length == 2 ? String.format("%02d:%02d", Integer.parseInt(object2[0]), Integer.parseInt(object2[1])) : null) : null;
       object3.mParcel.writeString((String)object);
       object3.mParcel.writeString((String)object2);
       this.riljLog(object3.serialString() + "> " + RIL.requestToString(object3.mRequest) + " " + n + " " + n2 + " " + n3 + " " + n4 + " " + (String)object + " - " + (String)object2);
       this.send((RILRequest)object3);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void setCallWaiting(boolean bl, int n, Message object) {
       object = RILRequest.obtain(36, (Message)object);
       object.mParcel.writeInt(2);
       Parcel parcel = object.mParcel;
       int n2 = bl ? 1 : 0;
       parcel.writeInt(n2);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + bl + ", " + n);
       this.send((RILRequest)object);
   }

   @Override
   public void setCdmaBroadcastActivation(boolean bl, Message message) {
       int n = 1;
       RILRequest rILRequest = RILRequest.obtain(94, message);
       rILRequest.mParcel.writeInt(1);
       message = rILRequest.mParcel;
       if (bl) {
           n = 0;
       }
       message.writeInt(n);
       this.riljLog(rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest));
       this.send(rILRequest);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void setCdmaBroadcastConfig(CdmaSmsBroadcastConfigInfo[] arrcdmaSmsBroadcastConfigInfo, Message object) {
       int n;
       object = RILRequest.obtain(93, (Message)object);
       Parcel parcel = new Parcel();
       for (CdmaSmsBroadcastConfigInfo cdmaSmsBroadcastConfigInfo : arrcdmaSmsBroadcastConfigInfo) {
           for (n = cdmaSmsBroadcastConfigInfo.getFromServiceCategory(); n <= cdmaSmsBroadcastConfigInfo.getToServiceCategory(); ++n) {
               parcel.add((CdmaSmsBroadcastConfigInfo)new CdmaSmsBroadcastConfigInfo(n, n, cdmaSmsBroadcastConfigInfo.getLanguage(), cdmaSmsBroadcastConfigInfo.isSelected()));
           }
       }
       arrcdmaSmsBroadcastConfigInfo = parcel.toArray(arrcdmaSmsBroadcastConfigInfo);
       object.mParcel.writeInt(arrcdmaSmsBroadcastConfigInfo.length);
       int n2 = 0;
       do {
           if (n2 >= arrcdmaSmsBroadcastConfigInfo.length) {
               this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " with " + arrcdmaSmsBroadcastConfigInfo.length + " configs : ");
               this.send((RILRequest)object);
               return;
           }
           object.mParcel.writeInt(arrcdmaSmsBroadcastConfigInfo[n2].getFromServiceCategory());
           object.mParcel.writeInt(arrcdmaSmsBroadcastConfigInfo[n2].getLanguage());
           parcel = object.mParcel;
           n = arrcdmaSmsBroadcastConfigInfo[n2].isSelected() ? 1 : 0;
           parcel.writeInt(n);
           ++n2;
       } while (true);
   }

   @Override
   public void setCdmaRoamingPreference(int n, Message object) {
       object = RILRequest.obtain(78, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " : " + n);
       this.send((RILRequest)object);
   }

   @Override
   public void setCdmaSubscriptionSource(int n, Message object) {
       object = RILRequest.obtain(77, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " : " + n);
       this.send((RILRequest)object);
   }

   @Override
   public void setCellInfoListRate(int n, Message object) {
       this.riljLog("setCellInfoListRate: " + n);
       object = RILRequest.obtain(110, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void setDataAllowed(boolean bl, Message object) {
       int n = 1;
       object = RILRequest.obtain(123, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(1);
       Parcel parcel = object.mParcel;
       if (!bl) {
           n = 0;
       }
       parcel.writeInt(n);
       this.send((RILRequest)object);
   }

   @Override
   public void setDataProfile(DataProfile[] arrdataProfile, Message object) {
       this.riljLog("Set RIL_REQUEST_SET_DATA_PROFILE");
       object = RILRequest.obtain(128, null);
       DataProfile.toParcel(object.mParcel, arrdataProfile);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " with " + arrdataProfile + " Data Profiles : ");
       for (int i = 0; i < arrdataProfile.length; ++i) {
           this.riljLog(arrdataProfile[i].toString());
       }
       this.send((RILRequest)object);
   }

   @Override
   public void setFacilityLock(String string, boolean bl, String string2, int n, Message message) {
       this.setFacilityLockForApp(string, bl, string2, n, null, message);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void setFacilityLockForApp(String string, boolean bl, String string2, int n, String string3, Message object) {
       RILRequest rILRequest = RILRequest.obtain(43, (Message)object);
       String string4 = rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest) + " [" + string + " " + bl + " " + n + " ";
       object = string4;
       if (!SHIP_BUILD) {
           object = string4 + string3;
       }
       this.riljLog((String)object + "]");
       rILRequest.mParcel.writeInt(5);
       rILRequest.mParcel.writeString(string);
       string = bl ? "1" : "0";
       rILRequest.mParcel.writeString(string);
       rILRequest.mParcel.writeString(string2);
       rILRequest.mParcel.writeString(Integer.toString(n));
       rILRequest.mParcel.writeString(string3);
       this.send(rILRequest);
   }

   @Override
   public void setGsmBroadcastActivation(boolean bl, Message message) {
       int n = 1;
       RILRequest rILRequest = RILRequest.obtain(91, message);
       rILRequest.mParcel.writeInt(1);
       message = rILRequest.mParcel;
       if (bl) {
           n = 0;
       }
       message.writeInt(n);
       this.riljLog(rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest));
       this.send(rILRequest);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void setGsmBroadcastConfig(SmsBroadcastConfigInfo[] arrsmsBroadcastConfigInfo, Message message) {
       int n;
       RILRequest rILRequest = RILRequest.obtain(90, message);
       int n2 = arrsmsBroadcastConfigInfo.length;
       rILRequest.mParcel.writeInt(n2);
       for (n = 0; n < n2; ++n) {
           rILRequest.mParcel.writeInt(arrsmsBroadcastConfigInfo[n].getFromServiceId());
           rILRequest.mParcel.writeInt(arrsmsBroadcastConfigInfo[n].getToServiceId());
           rILRequest.mParcel.writeInt(arrsmsBroadcastConfigInfo[n].getFromCodeScheme());
           rILRequest.mParcel.writeInt(arrsmsBroadcastConfigInfo[n].getToCodeScheme());
           message = rILRequest.mParcel;
           int n3 = arrsmsBroadcastConfigInfo[n].isSelected() ? 1 : 0;
           message.writeInt(n3);
       }
       this.riljLog(rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest) + " with " + n2 + " configs : ");
       n = 0;
       do {
           if (n >= n2) {
               this.send(rILRequest);
               return;
           }
           this.riljLog(arrsmsBroadcastConfigInfo[n].toString());
           ++n;
       } while (true);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void setICBarring(String string, int n, String string2, int n2, Message object) {
       object = RILRequest.obtain(10028, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " [" + string + " " + n + " " + n2 + " ]");
       object.mParcel.writeInt(5);
       object.mParcel.writeString(string);
       if (n == 0) {
           string = "0";
       } else if (n == 1) {
           string = "1";
       } else if (n == 3) {
           string = "3";
       } else if (n == 4) {
           string = "4";
       } else {
           Rlog.d((String)"RILJ", (String)"setICBarring: barring state is wrong value");
           string = "1";
       }
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       object.mParcel.writeString(Integer.toString(n2));
       object.mParcel.writeString("");
       this.send((RILRequest)object);
   }

   @Override
   public void setInitialAttachApn(String string, String string2, int n, String string3, String string4, Message object) {
       object = RILRequest.obtain(111, null);
       this.riljLog("Set RIL_REQUEST_SET_INITIAL_ATTACH_APN");
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       object.mParcel.writeInt(n);
       object.mParcel.writeString(string3);
       object.mParcel.writeString(string4);
       if (this.isDebugLevelNotLow()) {
           this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + ", apn:" + string + ", protocol:" + string2 + ", authType:" + n + ", username:" + string3 + ", password:" + string4);
       }
       this.send((RILRequest)object);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void setLocationUpdates(boolean bl, Message message) {
       int n = 1;
       RILRequest rILRequest = RILRequest.obtain(76, message);
       rILRequest.mParcel.writeInt(1);
       message = rILRequest.mParcel;
       if (!bl) {
           n = 0;
       }
       message.writeInt(n);
       this.riljLog(rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest) + ": " + bl);
       this.send(rILRequest);
   }

   @Override
   public void setLteBandMode(int n, Message object) {
       object = RILRequest.obtain(10024, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + n);
       this.send((RILRequest)object);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void setMute(boolean bl, Message object) {
       int n = 1;
       object = RILRequest.obtain(53, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + bl);
       object.mParcel.writeInt(1);
       Parcel parcel = object.mParcel;
       if (!bl) {
           n = 0;
       }
       parcel.writeInt(n);
       this.send((RILRequest)object);
   }

   @Override
   public void setNetworkSelectionModeAutomatic(Message object) {
       object = RILRequest.obtain(46, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void setNetworkSelectionModeManual(String string, Message object) {
       object = RILRequest.obtain(47, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + string);
       object.mParcel.writeString(string);
       this.send((RILRequest)object);
   }

   @Override
   public void setOnNITZTime(Handler handler, int n, Object object) {
       super.setOnNITZTime(handler, n, object);
       if (this.mLastNITZTimeInfo != null) {
           this.mNITZTimeRegistrant.notifyRegistrant(new AsyncResult((Object)null, this.mLastNITZTimeInfo, null));
           this.mLastNITZTimeInfo = null;
       }
   }

   @Override
   public void setPhoneType(int n) {
       this.riljLog("setPhoneType=" + n + " old value=" + this.mPhoneType);
       this.mPhoneType = n;
   }

   @Override
   public void setPreferredNetworkList(int n, String string, String string2, int n2, int n3, int n4, int n5, Message object) {
       object = RILRequest.obtain(10015, (Message)object);
       object.mParcel.writeInt(n);
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       object.mParcel.writeInt(n2);
       object.mParcel.writeInt(n3);
       object.mParcel.writeInt(n4);
       object.mParcel.writeInt(n5);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + ", " + n + ", " + string + ", " + string2 + ", " + n2 + "," + n3 + ", " + n4 + ", " + n5);
       this.send((RILRequest)object);
   }

   @Override
   public void setPreferredNetworkType(int n, Message object) {
       object = RILRequest.obtain(73, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.setInitialPhoneType(n);
       this.mPreferredNetworkType = n;
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " : " + n);
       this.send((RILRequest)object);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void setPreferredVoicePrivacy(boolean bl, Message message) {
       int n = 1;
       RILRequest rILRequest = RILRequest.obtain(82, message);
       rILRequest.mParcel.writeInt(1);
       message = rILRequest.mParcel;
       if (!bl) {
           n = 0;
       }
       message.writeInt(n);
       this.riljLog(rILRequest.serialString() + "> " + RIL.requestToString(rILRequest.mRequest) + " " + bl);
       this.send(rILRequest);
   }

   @Override
   public void setRadioCapability(RadioCapability radioCapability, Message object) {
       object = RILRequest.obtain(131, (Message)object);
       object.mParcel.writeInt(radioCapability.getVersion());
       object.mParcel.writeInt(radioCapability.getSession());
       object.mParcel.writeInt(radioCapability.getPhase());
       object.mParcel.writeInt(radioCapability.getRadioAccessFamily());
       object.mParcel.writeString(radioCapability.getLogicalModemUuid());
       object.mParcel.writeInt(radioCapability.getStatus());
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + radioCapability.toString());
       this.send((RILRequest)object);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void setRadioPower(boolean bl, Message object) {
       int n = 1;
       RILRequest rILRequest = RILRequest.obtain(23, (Message)object);
       rILRequest.mParcel.writeInt(1);
       object = rILRequest.mParcel;
       if (!bl) {
           n = 0;
       }
       object.writeInt(n);
       if (bl) {
           Rlog.i((String)"RILJ", (String)"!@Boot_SVC : setRadioPower on");
       }
       StringBuilder stringBuilder = new StringBuilder().append(rILRequest.serialString()).append("> ").append(RIL.requestToString(rILRequest.mRequest));
       object = bl ? " on" : " off";
       this.riljLog(stringBuilder.append((String)object).toString());
       this.send(rILRequest);
   }

   @Override
   public void setSimInitEvent(Message object) {
       object = RILRequest.obtain(10014, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void setSimPower(int n, Message object) {
       object = RILRequest.obtain(10023, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " int : " + n);
       this.send((RILRequest)object);
   }

   @Override
   public void setSmscAddress(String string, Message object) {
       object = RILRequest.obtain(101, (Message)object);
       object.mParcel.writeString(string);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " : " + string);
       this.send((RILRequest)object);
   }

   /*
    * Enabled aggressive block sorting
    */
   @Override
   public void setSuppServiceNotifications(boolean bl, Message object) {
       int n = 1;
       object = RILRequest.obtain(62, (Message)object);
       object.mParcel.writeInt(1);
       Parcel parcel = object.mParcel;
       if (!bl) {
           n = 0;
       }
       parcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void setTTYMode(int n, Message object) {
       object = RILRequest.obtain(80, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " : " + n);
       this.send((RILRequest)object);
   }

   @Override
   public void setTransmitPower(int n, Message object) {
       object = RILRequest.obtain(10007, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + n);
       this.send((RILRequest)object);
   }

   @Override
   public void setUiccSubscription(int n, int n2, int n3, int n4, Message object) {
       object = RILRequest.obtain(122, (Message)object);
       Rlog.i((String)"RILJ", (String)"!@Boot_SVC : setUiccSubscription");
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " slot: " + n + " appIndex: " + n2 + " subId: " + n3 + " subStatus: " + n4);
       object.mParcel.writeInt(n);
       object.mParcel.writeInt(n2);
       object.mParcel.writeInt(n3);
       object.mParcel.writeInt(n4);
       this.send((RILRequest)object);
   }

   @Override
   public void setVoiceDomainPref(int n, Message object) {
       object = RILRequest.obtain(10005, (Message)object);
       object.mParcel.writeInt(1);
       object.mParcel.writeInt(n);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + n);
       this.send((RILRequest)object);
   }

   @Override
   public void setupDataCall(String string, String string2, String string3, String string4, String string5, String string6, String string7, Message object) {
       object = RILRequest.obtain(27, (Message)object);
       object.mParcel.writeInt(7);
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       object.mParcel.writeString(string3);
       object.mParcel.writeString(string4);
       object.mParcel.writeString(string5);
       object.mParcel.writeString(string6);
       object.mParcel.writeString(string7);
       Rlog.i((String)"RILJ", (String)"!@Boot_SVC : setupDataCall");
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " " + string + " " + string2 + " " + string3 + " " + string4 + " " + string5 + " " + string6 + " " + string7);
       this.send((RILRequest)object);
   }

   /*
    * Enabled force condition propagation
    * Lifted jumps to return sites
    */
   @Override
   public void startDtmf(char c, Message object) {
       if (this.mDtmfProgress >= 200) {
           this.riljLog("DTMF request warning.Fixed Count=" + this.mDtmfProgress);
           object = new Intent("com.samsung.intent.action.ACTION_DTMF_BUSY");
           this.mContext.sendBroadcast((Intent)object);
           do {
               return;
               break;
           } while (true);
       }
       object = RILRequest.obtain(49, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeString(Character.toString(c));
       this.send((RILRequest)object);
       ++this.mDtmfProgress;
   }

   @Override
   public void stopDtmf(Message object) {
       object = RILRequest.obtain(50, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
       ++this.mDtmfProgress;
   }

   @Override
   public void supplyIccPerso(String string, Message object) {
       object = RILRequest.obtain(10018, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(1);
       object.mParcel.writeString(string);
       this.send((RILRequest)object);
   }

   @Override
   public void supplyIccPin(String string, Message message) {
       this.supplyIccPinForApp(string, null, message);
   }

   @Override
   public void supplyIccPin2(String string, Message message) {
       this.supplyIccPin2ForApp(string, null, message);
   }

   @Override
   public void supplyIccPin2ForApp(String string, String string2, Message object) {
       object = RILRequest.obtain(4, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(2);
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       this.send((RILRequest)object);
   }

   @Override
   public void supplyIccPinForApp(String string, String string2, Message object) {
       object = RILRequest.obtain(2, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(2);
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       this.send((RILRequest)object);
   }

   @Override
   public void supplyIccPuk(String string, String string2, Message message) {
       this.supplyIccPukForApp(string, string2, null, message);
   }

   @Override
   public void supplyIccPuk2(String string, String string2, Message message) {
       this.supplyIccPuk2ForApp(string, string2, null, message);
   }

   @Override
   public void supplyIccPuk2ForApp(String string, String string2, String string3, Message object) {
       object = RILRequest.obtain(5, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(3);
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       object.mParcel.writeString(string3);
       this.send((RILRequest)object);
   }

   @Override
   public void supplyIccPukForApp(String string, String string2, String string3, Message object) {
       object = RILRequest.obtain(3, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(3);
       object.mParcel.writeString(string);
       object.mParcel.writeString(string2);
       object.mParcel.writeString(string3);
       this.send((RILRequest)object);
   }

   @Override
   public void supplyNetworkDepersonalization(String string, int n, Message object) {
       object = RILRequest.obtain(8, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest) + " Type:" + "PERSOSUBSTATE_SIM_NETWORK");
       object.mParcel.writeInt(n);
       object.mParcel.writeString(string);
       this.send((RILRequest)object);
   }

   @Override
   public void supplyNetworkDepersonalization(String string, Message object) {
       object = RILRequest.obtain(8, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       object.mParcel.writeInt(1);
       object.mParcel.writeString(string);
       this.send((RILRequest)object);
   }

   @Override
   public void switchWaitingOrHoldingAndActive(Message object) {
       object = RILRequest.obtain(15, (Message)object);
       this.riljLog(object.serialString() + "> " + RIL.requestToString(object.mRequest));
       this.send((RILRequest)object);
   }

   @Override
   public void testingEmergencyCall() {
       this.riljLog("testingEmergencyCall");
       this.mTestingEmergencyCall.set(true);
   }

   @Override
   public void uiccGbaAuthenticateBootstrap(String string, byte[] object, byte[] object2, Message object3) {
       object3 = RILRequest.obtain(10025, (Message)object3);
       object = IccUtils.bytesToHexString((byte[])object);
       object2 = IccUtils.bytesToHexString((byte[])object2);
       this.riljLog(object3.serialString() + "> " + RIL.requestToString(object3.mRequest) + "[" + string + "," + (String)object + "," + (String)object2 + "]");
       object3.mParcel.writeString(string);
       object3.mParcel.writeString((String)object);
       object3.mParcel.writeString((String)object2);
       this.send((RILRequest)object3);
   }

   @Override
   public void uiccGbaAuthenticateNaf(String string, byte[] object, byte[] object2, Message object3) {
       object3 = RILRequest.obtain(10026, (Message)object3);
       object = IccUtils.bytesToHexString((byte[])object);
       object2 = IccUtils.bytesToHexString((byte[])object2);
       this.riljLog(object3.serialString() + "> " + RIL.requestToString(object3.mRequest) + "[" + string + "," + (String)object + "]");
       object3.mParcel.writeString(string);
       object3.mParcel.writeString((String)object);
       object3.mParcel.writeString((String)object2);
       this.send((RILRequest)object3);
   }

   /*
    * Unable to fully structure code
    * Enabled aggressive block sorting
    * Enabled unnecessary exception pruning
    * Enabled aggressive exception aggregation
    * Lifted jumps to return sites
    */
   @Override
   public void updateStackBinding(int var1_1, int var2_2, Message var3_3) {
       var5_7 = new ByteArrayOutputStream();
       var4_8 = new DataOutputStream(var5_7);
       Rlog.d((String)"RILJ", (String)"updateStackBinding");
       var4_8.writeByte(2);
       var4_8.writeByte(98);
       var4_8.writeShort(6);
       var4_8.writeByte(var1_1);
       var4_8.writeByte(var2_2);
       var4_8.close();
       ** GOTO lbl26
       {
           catch (Exception var4_9) {}
       }
       catch (IOException var3_4) {
           try {
               var4_8.close();
               return;
           }
           catch (Exception var3_5) {
               return;
           }
       }
       catch (Throwable var3_6) {
           try {
               var4_8.close();
           }
           catch (Exception var4_10) {
               throw var3_6;
           }
           throw var3_6;
lbl26: // 2 sources:
           this.invokeOemRilRequestRaw(var5_7.toByteArray(), var3_3);
           return;
       }
   }

   @Override
   public void writeSmsToRuim(int n, String string, Message object) {
       n = this.translateStatus(n);
       object = RILRequest.obtain(96, (Message)object);
       object.mParcel.writeInt(n);
       object.mParcel.writeString(string);
       this.send((RILRequest)object);
   }

   @Override
   public void writeSmsToSim(int n, String string, String string2, Message object) {
       n = this.translateStatus(n);
       object = RILRequest.obtain(63, (Message)object);
       object.mParcel.writeInt(n);
       object.mParcel.writeString(string2);
       object.mParcel.writeString(string);
       this.send((RILRequest)object);
   }

   class RILReceiver
   implements Runnable {
       byte[] buffer;

       RILReceiver() {
           this.buffer = new byte[9216];
       }

       /*
        * Exception decompiling
        */
       @Override
       public void run() {
           // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
           // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
           // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:374)
           // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:452)
           // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2877)
           // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:825)
           // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
           // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
           // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
           // org.benf.cfr.reader.entities.Method.analyse(Method.java:355)
           // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:768)
           // org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:681)
           // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:764)
           // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:700)
           // org.benf.cfr.reader.Main.doJar(Main.java:134)
           // org.benf.cfr.reader.Main.main(Main.java:189)
           throw new IllegalStateException("Decompilation failed");
       }
   }

   class RILSender
   extends Handler
   implements Runnable {
       byte[] dataLength;

       public RILSender(Looper looper) {
           super(looper);
           this.dataLength = new byte[4];
       }

       /*
        * Exception decompiling
        */
       public void handleMessage(Message var1_1) {
           // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
           // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [3[TRYBLOCK]], but top level block is 5[TRYBLOCK]
           // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:397)
           // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:449)
           // org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2877)
           // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:825)
           // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
           // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
           // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
           // org.benf.cfr.reader.entities.Method.analyse(Method.java:355)
           // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:768)
           // org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:681)
           // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:764)
           // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:700)
           // org.benf.cfr.reader.Main.doJar(Main.java:134)
           // org.benf.cfr.reader.Main.main(Main.java:189)
           throw new IllegalStateException("Decompilation failed");
       }

       @Override
       public void run() {
       }
   }

   public final class UnsolOemHookBuffer {
       private byte[] mData;
       private int mRilInstance;

       public UnsolOemHookBuffer(int n, byte[] arrby) {
           this.mRilInstance = n;
           this.mData = arrby;
       }

       public int getRilInstance() {
           return this.mRilInstance;
       }

       public byte[] getUnsolOemHookBuffer() {
           return this.mData;
       }
   }

}
