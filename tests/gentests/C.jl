public class C {
    int{Alice:} aliceInt;
    int{Bob:} bobInt;
    int{} publicInt;

    void api{}()
    {
	violateAlice();
	violateBob();
    }

    void violateAlice() {
	publicInt = aliceInt;
    }

    void violateBob() {
	publicInt = bobInt;
    }
}
