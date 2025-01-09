package com.swiftmessage.swiftmessage.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swiftmessage.swiftmessage.repository.TblCorrespondentBankRepository;
import com.swiftmessage.swiftmessage.entity.TblCorrespondentBank;

@Service
public class TblCorrespondentBankService {
    @Autowired
    TblCorrespondentBankRepository correspondentJPA;


    public List<TblCorrespondentBank> findByName(String name)
    {
        return correspondentJPA.findByName(name);
    }
    public TblCorrespondentBank saveNew(TblCorrespondentBank correspondent)
    {
       return correspondentJPA.save(correspondent);
    }


    public List<TblCorrespondentBank> findAll() {
        return correspondentJPA.findAll();
    }

    public List<TblCorrespondentBank> findByCode(String code1)
    {
        return correspondentJPA.findByCode(code1);
    }

    public List<TblCorrespondentBank> findByBankLike(String bank,String currency)
    {
        return correspondentJPA.findByLikeCode(bank,currency);
    }


    public List<TblCorrespondentBank> findBycodeandname(String code)
    {
        return correspondentJPA.findByCodeandname(code);
    }


    public void delete(TblCorrespondentBank correspondent)
    {
        correspondentJPA.deleteById(correspondent.getId());
    }
    public TblCorrespondentBank update(TblCorrespondentBank correspondent) {
        TblCorrespondentBank sh = correspondentJPA.findById(correspondent.getId()).orElse(null);
        sh = correspondent;
        return correspondentJPA.save(sh);

    }

    public TblCorrespondentBank findbyID(BigDecimal id) {

        return correspondentJPA.findById(id).orElse(null);

    }


}