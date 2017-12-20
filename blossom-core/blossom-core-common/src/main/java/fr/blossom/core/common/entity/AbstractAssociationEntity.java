package fr.blossom.core.common.entity;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Abstract entity class for representing n-n relationships.<br/>
 *
 * @param <A> the first {@code AbstractEntity} type
 * @param <B> the second {@code AbstractEntity} type
 * @author Maël Gargadennnec
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class AbstractAssociationEntity<A extends AbstractEntity, B extends AbstractEntity> extends AbstractEntity {

    public abstract A getA();

    public abstract void setA(A a);

    public abstract B getB();

    public abstract void setB(B b);

    @Override
    public String toString() {
        return "AbstractAssociationEntity{" +
                "a=" + getA() +
                ", b=" + getB() +
                '}';
    }
}
