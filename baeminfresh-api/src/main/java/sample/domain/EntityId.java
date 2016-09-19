package sample.domain;

import java.io.Serializable;

/**
 * @author ykpark@woowahan.com
 */
public interface EntityId<ID extends Serializable> extends Serializable {

    ID getId();

}
