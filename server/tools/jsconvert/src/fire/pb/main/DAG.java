package fire.pb.main;

public class DAG<T extends Comparable<T> > {
	public static interface IWalk<T extends Comparable<T> > {
		public void onNode(DAGNode<T> n);
	}

	private final java.util.TreeSet<DAGNode<T> > nodes;

	public DAG() {
		nodes = new java.util.TreeSet<DAGNode<T> >();
	}
	
	DAGNode<T>  createNodeIfNotExist(T name) {
		DAGNode<T>  n;
		if ((n = getNode(name)) != null)
			return n;
		n = new DAGNode<T> (name, this);
		nodes.add(n);
		return n;
	}
	
	DAGNode<T>  getNode(T name) {
		for (final DAGNode<T>  n : nodes) {
			if (n.getName().equals(name))
				return n;
		}
		return null;
	}

	
	void walk(IWalk<T> w) {
		final java.util.LinkedList<DAGNode<T> > walked = new java.util.LinkedList<DAGNode<T> >();
		for (final DAGNode<T>  n : nodes) {
			if (walked.contains(n))
				continue;
			DAGNode<T>  curr = n;
			while (true) {
				final java.util.LinkedList<DAGNode<T> > parents = new java.util.LinkedList<DAGNode<T> >();
				for(int i=0;i!=curr.getPrevs().size();){
					final DAGNode<T>  p=curr.getPrevs().get(i);
					if (walked.contains(p)){
						++i;
						continue;
					}
					if (parents.contains(p))
						throw new RuntimeException("找到循环");
					parents.push(curr);
					curr = p;
					i=0;
				}

				w.onNode(curr);
				walked.add(curr);

				if (!parents.isEmpty()) {
					curr = parents.pollLast();
					continue;
				} else {
					for (final DAGNode<T>  p : curr.getNexts()) {
						if (walked.contains(p))
							continue;
						curr=p;
					}
					break;
				}
			}
		}
	}

}
