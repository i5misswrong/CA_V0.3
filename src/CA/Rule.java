package CA;



import java.util.Vector;



public class Rule  {

	Vector<peoOld> people=new Vector<peoOld>();

	public Vector<peoOld> getPeople() {

		return people;

	}

	public void setPeople(Vector<peoOld> people) {

		this.people = people;

	}

	public void testV(){

		System.out.println("peo的尺寸"+people.size());

	}

	

}

