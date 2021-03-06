package jif.lang;

import java.util.*;

public class WriterPolicy extends AbstractPolicy implements IntegPolicy
{
    private final Principal owner;
    private final Principal writer;
    
    public WriterPolicy(LabelUtil labelUtil, Principal owner, Principal writer) {
        super(labelUtil);
        this.owner = owner;
        this.writer = writer;
    }
    
    public Principal owner() {
        return owner;
    }
    
    public Principal writer() {
        return writer;
    }
    
    
    public boolean relabelsTo(Policy p, Set s) {
        if (this == p || this.equals(p)) return true;
        if (p instanceof JoinIntegPolicy) {
            JoinPolicy jp = (JoinPolicy)p;
            // this <= p1 join ... join p2 if there exists a pi such that
            // this <= pi
            for (Iterator iter = jp.joinComponents().iterator(); iter.hasNext();) {
                Policy pi = (Policy)iter.next();
                if (labelUtil.relabelsTo(this, pi, s)) return true;                
            }
            return false;
        }
        else if (p instanceof MeetIntegPolicy) {
            MeetPolicy mp = (MeetPolicy)p;
            // this <= p1 meet ... meet p2 if for all pi 
            // this <= pi
            Set temp = new HashSet();
            for (Iterator iter = mp.meetComponents().iterator(); iter.hasNext();) {
                Policy pi = (Policy)iter.next();
                if (!labelUtil.relabelsTo(this, pi, temp)) return false;                
            }
            s.addAll(temp);
            return true;            
        }
        else if (!(p instanceof WriterPolicy))
            return false;
        
        WriterPolicy pp = (WriterPolicy) p;
        
        // this = { o  : .. ri  .. }
        // p    = { o' : .. rj' .. }
        
        // o' >= o?
        
        ActsForProof ownersProof = PrincipalUtil.actsForProof(owner, pp.owner);
        if (ownersProof == null) {
            return false;
        }
        
        // for all j . rj' >= o || exists i . rj' >= ri
        ActsForProof writerWriterProof = PrincipalUtil.actsForProof(this.writer, pp.writer);
        if (writerWriterProof != null) {
            ownersProof.gatherDelegationDependencies(s);
            writerWriterProof.gatherDelegationDependencies(s);
            return true;
        }
        ActsForProof writerOwnerProof = PrincipalUtil.actsForProof(this.writer, pp.owner);
        if (writerOwnerProof != null) {
            ownersProof.gatherDelegationDependencies(s);
            writerOwnerProof.gatherDelegationDependencies(s);      
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        return (owner==null?0:owner.hashCode()) ^ (writer==null?0:writer.hashCode()) ^ -124978;
    }
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof WriterPolicy)) {
            return false;
        }
        
        WriterPolicy policy = (WriterPolicy) o;
        
        if (owner == policy.owner || (owner != null && owner.equals(policy.owner)
                && policy.owner != null && policy.owner.equals(owner))) {
            return (writer == policy.writer || (writer != null && writer.equals(policy.writer)
                    && policy.writer != null && policy.writer.equals(writer)));
        }
        
        return false;
    }
    
    public String toString() {
        String str = PrincipalUtil.toString(owner) + "!: ";
        if (!PrincipalUtil.isTopPrincipal(writer))
            str += PrincipalUtil.toString(writer);
        return str;
    }

    public IntegPolicy join(IntegPolicy p, Set s) {
        return labelUtil.join(this, p, s);
    }

    public IntegPolicy meet(IntegPolicy p, Set s) {
        return labelUtil.meet(this, p, s);
    }    
    public IntegPolicy join(IntegPolicy p) {
        return labelUtil.join(this, p);
    }

    public IntegPolicy meet(IntegPolicy p) {
        return labelUtil.meetPol(this, p);
    }    
}
