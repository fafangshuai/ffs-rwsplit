package ffs.common.datasource;

import ffs.facade.domain.Person;
import ffs.platform.mapper.PersonMapper;
import ffs.platform.service.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:conf/applicationContext.xml" })
public class RoutingDataSourceTest {
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonMapper personMapper;

    @Test
    public void test() throws Exception {

        List<Person> list = personService.findList();
        for (Person item : list) {
            System.out.println(item);
        }

        List<Person> idolList = personService.findAfterInsert(new Person("元谋人", 16));
        for (Person item : idolList) {
            System.out.println(item);
        }

        /*System.out.println(personMapper.getById(25));
        personMapper.insert(new Person("北京人", 18));

        List<Person> list = personMapper.getList();
        for (Person item : list) {
            System.out.println(item);
        }*/
    }
}
