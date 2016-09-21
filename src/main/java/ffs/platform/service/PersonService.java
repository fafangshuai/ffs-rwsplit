package ffs.platform.service;

import ffs.common.datasource.DataSource;
import ffs.facade.domain.Person;
import ffs.platform.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonMapper personMapper;

//    @Transactional(rollbackFor = Throwable.class)
    @DataSource(value = DataSource.Type.MASTER, isHold = true)
    public List<Person> findAfterInsert(Person person) {
        personMapper.insert(person);
        return personMapper.getList();
    }

    public List<Person> findList() {
        return personMapper.getList();
    }
}
