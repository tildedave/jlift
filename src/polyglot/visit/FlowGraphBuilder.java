package polyglot.visit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.util.IdentityKey;

public class FlowGraphBuilder extends FlowGraph {

	public FlowGraphBuilder(Term root, boolean forward) {
		super(root, forward);
	}

    @Override
	public Map pathMap(Node n) {
        return (Map) peerMap.get(new JLiftIdentityKey(n));
    }

    @Override
	public Collection peers(Term n, int entry) {
        IdentityKey k = new JLiftIdentityKey(n);
        Map pathMap = (Map) peerMap.get(k);

        if (pathMap == null) {
            return Collections.EMPTY_LIST;
        }

        Collection peers = pathMap.values();
        List l = new ArrayList(peers.size());

        for (Iterator i = peers.iterator(); i.hasNext();) {
            Peer p = (Peer) i.next();

            if (p.entry == entry) {
                l.add(p);
            }
        }

        return l;
    }

    @Override
	public Peer peer(Term n, List path_to_finally, int entry) {
        IdentityKey k = new JLiftIdentityKey(n);
        Map pathMap = (Map) peerMap.get(k);

//        for (IdentityKey k1 : (Collection<IdentityKey>) peerMap.keySet()) {
//        	System.out.println(k1 + " vs " + k);
//        	System.out.println(k1.object().equals(k.object()));
//        	System.out.println(((Node) k1.object()).position() + " vs " + ((Node) k.object()).position());
//        }

        if (pathMap == null) {
            pathMap = new HashMap();
            peerMap.put(k, pathMap);
        }

        PeerKey lk = new PeerKey(path_to_finally, entry);
        Peer p = (Peer) pathMap.get(lk);

        if (p == null) {
            p = new Peer(n, path_to_finally, entry);
            pathMap.put(lk, p);
        }

        return p;
    }


}
