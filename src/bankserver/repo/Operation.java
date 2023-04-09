package bankserver.repo;

import model.Account;

public interface Operation
{
    Object create(String... args);

    Object read(String... args);

    Object update(String... args);

    Object delete(String... args);

}
