package ITC.Model;

import java.util.ArrayList;

public class TravelsITC extends ArrayList<TravelITC> {
	public TravelsITC() {
		super();
	}

	public int containsId(String id) {
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i).getRoom().equals(id)) {
				return i;
			}
		}
		return -1;
	}
}
