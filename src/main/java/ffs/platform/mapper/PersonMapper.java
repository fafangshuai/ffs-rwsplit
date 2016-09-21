package ffs.platform.mapper;

import ffs.common.datasource.DataSource;
import ffs.facade.domain.Person;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * Created by ffs on 15-5-27.
 */
public interface PersonMapper {

    @DataSource(DataSource.Type.SLAVE)
    Integer insert(Person person);

    Integer update(Person person);

    Integer delete(@Param("id") Integer id);

    Person getById(@Param("id") Integer id);

    List<Person> getList();
}
