package fr.mgargadennec.blossom.core.common.dto;

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
