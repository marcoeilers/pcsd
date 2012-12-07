package client;

import dk.diku.pcsd.assignment1.impl.*;

public class MyClient {

	public static void main(String[] args) {
		KeyValueBaseImplServiceService kvbiss = new KeyValueBaseImplServiceService();
		KeyValueBaseImplService kvbis = kvbiss.getKeyValueBaseImplServicePort();
		
		try {
		
		
			KeyImpl key = new KeyImpl();
			key.setKey("testKey");
			
			ValueImpl value = new ValueImpl();
			value.setValue("testValue");
			
			ValueListImpl valueList = new ValueListImpl();
			valueList.getValueList().add(value);
			
			KeyImpl key2 = new KeyImpl();
			key.setKey("testKey2");
			
			ValueImpl value2 = new ValueImpl();
			value.setValue("testValue2");
			
			ValueListImpl valueList2 = new ValueListImpl();
			valueList.getValueList().add(value2);
			
			//kvbis.insert(key, valueList);
			//kvbis.insert(key2, valueList2);
			//kvbis.update(key, valueList2);
			//kvbis.delete(key2);
			
			System.out.println("Output: " + kvbis.read(key).getValueList().get(0).getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
