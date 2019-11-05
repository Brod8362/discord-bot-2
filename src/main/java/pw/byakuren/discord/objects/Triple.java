package pw.byakuren.discord.objects;

public class Triple<A,B,C> {

    public final A a;
    public final B b;
    public final C c;

    public Triple(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Triple)) return false;
        Triple t = (Triple)obj;
        return (t.a.equals(a) && t.b.equals(b) && t.c.equals(c));
    }
}
