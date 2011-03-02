/*
 * File          : JLiftPairLabel_c.java
 * Project       : jlift
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 1, 2008 4:45:53 PM
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jlift.types.label;

import jif.translate.LabelToJavaExpr;
import jif.types.JifTypeSystem;
import jif.types.hierarchy.LabelEnv;
import jif.types.label.ConfPolicy;
import jif.types.label.IntegPolicy;
import jif.types.label.Label;
import jif.types.label.PairLabel;
import jif.types.label.PairLabel_c;
import jlift.JLiftOptions;
import polyglot.types.TypeObject;
import polyglot.util.Position;

public class JLiftPairLabel_c extends PairLabel_c {

	public JLiftPairLabel_c(JifTypeSystem ts, ConfPolicy confPolicy,
			IntegPolicy integPolicy, Position pos, LabelToJavaExpr trans) {
		super(ts, confPolicy, integPolicy, pos, trans);
	}

    @Override
	public boolean isBottom() {
    	if (JLiftOptions.getInstance().noIntegrity()) {
    		return confPolicy().isBottom();
    	}
    	else {
        return confPolicy().isBottom() && integPolicy().isBottom();
    	}
    }

    @Override
	public boolean isTop() {
    	if (JLiftOptions.getInstance().noIntegrity()) {
    		return confPolicy().isTop();
    	}
    	else {
    		return confPolicy().isTop() && integPolicy().isTop();
    	}
    }

    @Override
	public boolean equalsImpl(TypeObject o) {
        if (this == o) return true;
        if (! (o instanceof PairLabel_c)) {
            return false;
        }
        PairLabel_c that = (PairLabel_c) o;
    	if (JLiftOptions.getInstance().noIntegrity()) {
    		return this.confPolicy().equals(that.confPolicy());
    	}
    	else {
    		return (this.confPolicy().equals(that.confPolicy())) &&
    			   (this.integPolicy().equals(that.integPolicy()));
    	}
    }

    @Override
	public boolean leq_(Label L, LabelEnv env, LabelEnv.SearchState state) {
        if (L instanceof PairLabel) {
        	PairLabel that = (PairLabel)L;

            if (L.toString().contains("incomparable") && !this.isBottom())
            	return false;
            if (this.toString().contains("incomparable"))
            	return false;
            if (JLiftOptions.getInstance().noIntegrity()) {
            	return env.leq(this.confPolicy(), that.confPolicy(), state);
            }
            else {
            	return env.leq(this.confPolicy(), that.confPolicy(), state) &&
            		   env.leq(this.integPolicy(), that.integPolicy(), state);
            }
        }
        return false;
    }
}
