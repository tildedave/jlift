/*
 * File          : JLiftParsedPolyType.java
 * Project       : jlift
 * Description   : Extension of JifParsedPolyType that keeps track of
 * 				   interfaces/subtypes/implementers.
 * Author(s)     : dhking
 *
 * Created       : Dec 4, 2007 9:38:57 AM
 *
 * Copyright (c) 2007 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */

package jlift.types;

import java.util.List;

import jif.types.JifParsedPolyType;
import polyglot.types.ReferenceType;

public interface JLiftParsedPolyType extends JifParsedPolyType {

    void addSubtype(ReferenceType t);
    List<ReferenceType> getSubtypes();
    void addImplementer(ReferenceType t);
    List<ReferenceType> getImplementers();
}
