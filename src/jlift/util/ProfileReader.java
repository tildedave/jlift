package jlift.util;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

import jlift.JLiftOptions;
import noNamespace.FrameDocument;
import noNamespace.InteractionDocument;
import noNamespace.ProfileDocument;
import noNamespace.ThreadDocument;

import org.apache.xmlbeans.XmlException;

import polyglot.util.InternalCompilerError;

public class ProfileReader {
	public HashMap<String, BigInteger> readProfile() {
		String filePath = JLiftOptions.getInstance().getProfileXMLFile();

		java.io.File inputXMLFile = new java.io.File(filePath);
		ProfileDocument profileDoc;
		try {
			profileDoc = ProfileDocument.Factory.parse(inputXMLFile);
		} catch (XmlException e) {
			throw new InternalCompilerError(e);
		} catch (IOException e) {
			throw new InternalCompilerError(e);
		}

		ProfileDocument.Profile profileElement= profileDoc.getProfile();
		ThreadDocument.Thread[] threads  = profileElement.getThreadArray();
		InteractionDocument.Interaction[] interactions;
		FrameDocument.Frame singleframe;

		HashMap<String, BigInteger> profilerValues = new HashMap<String, BigInteger>();

		for (int j = 0; j < threads.length; j++) {
			System.out.println("Thread Id is "+ threads[j].getId());
			interactions = threads[j].getInteractionArray();
			for(int k = 0; k < interactions.length;k++) {
				singleframe = interactions[k].getFrame();
				readFrame(singleframe, profilerValues);
			}
		}

	/*	for (Enumeration<String> e = profilerValues.keys (); e.hasMoreElements ();) {
			String keys = e.nextElement().toString();
			// ???????
		}*/

		return profilerValues;
	}


	private int readFrame(FrameDocument.Frame frame, HashMap<String, BigInteger> profilerValues)
	{
		String key = frame.getMn();
		BigInteger value = frame.getC();

		if(!(profilerValues.containsKey(key))) {
			profilerValues.put(key, value);
		}
		else {
			BigInteger original = profilerValues.get(key);
			value = (original).add(value);
			profilerValues.remove(key);
			profilerValues.put(key,value);
		}


		if(frame.sizeOfFrameArray()!=0) {
			FrameDocument.Frame[] frames = frame.getFrameArray();
			for(int i=0; i < frames.length;i++) {
				readFrame(frames[i], profilerValues);
			}
		}
		return 0;
	}
}
