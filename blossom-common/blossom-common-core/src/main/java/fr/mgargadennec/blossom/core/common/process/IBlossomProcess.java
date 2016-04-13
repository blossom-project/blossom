package fr.mgargadennec.blossom.core.common.process;

public interface IBlossomProcess<I, O extends BlossomAbstractProcessDTO> {

  O createProcessDTOfromPO(I i);

  I createPOfromProcessDTO(O o);

}
