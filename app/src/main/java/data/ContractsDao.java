package data;

import java.util.List;

import loans.model.Datum;

public interface ContractsDao {
    List<Datum> getContracts();

    void deleteDatum(Datum datum);

    Long insertDatum(Datum datum);

    public void deleteAll();

}
