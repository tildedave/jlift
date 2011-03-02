/*
 * File          : JLiftDefaultSignature.java
 * Project       : jlift
 * Description   : Interface that provides for a defaultArrayBaseLabel with a position.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 8:54:47 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import jif.types.DefaultSignature;
import jif.types.label.Label;
import polyglot.util.Position;

public interface JLiftDefaultSignature extends DefaultSignature
{
	Label defaultArrayBaseLabel(Position pos);
}
