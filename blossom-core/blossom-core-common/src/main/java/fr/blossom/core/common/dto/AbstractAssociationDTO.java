package fr.blossom.core.common.dto;

/**
 * Abstract DTO class for representing n-n relationships between {@code A} and {@code A} {@link AbstractDTO}
 *
 * @param <A>
 * @param <B>
 * @author Maël Gargadennec
 */
public abstract class AbstractAssociationDTO<A extends AbstractDTO, B extends AbstractDTO> extends AbstractDTO {
    private A a;
    private B b;

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }
}
