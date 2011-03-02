/*
 * File          : JLiftLabelCheckingUtil.java
 * Project       : jlift
 * Description   : <Insert Description>
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:55:26 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.util.Stack;

import jif.types.JifContext;
import jif.visit.LabelChecker;

public class JLiftLabelCheckingUtil {
	public static LabelChecker global;
	public static Stack<JifContext> globalContextStack = new Stack<JifContext>();
}
