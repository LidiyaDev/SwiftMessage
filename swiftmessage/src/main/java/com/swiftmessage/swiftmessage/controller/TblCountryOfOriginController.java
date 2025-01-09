package com.swiftmessage.swiftmessage.controller;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.swiftmessage.swiftmessage.entity.TblCountryOfOrigin;
import com.swiftmessage.swiftmessage.service.TblCountryOfOriginService;

@Controller
@ManagedBean
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Named("tblCountryOfOriginController")
public class TblCountryOfOriginController {
    @Autowired
    TblCountryOfOriginService countryService;

    private TblCountryOfOrigin selected;
    private List<TblCountryOfOrigin> items;

    private String cname;
    private String ccode;

    
    public String getCname() {
        return cname;
    }
    public void setCname(String cname) {
        this.cname = cname;
    }
    public String getCcode() {
        return ccode;
    }
    public void setCcode(String ccode) {
        this.ccode = ccode;
    }
    public TblCountryOfOrigin getSelected() {
        return selected;
    }
    public void setSelected(TblCountryOfOrigin selected) {
        this.selected = selected;
    }
    public List<TblCountryOfOrigin> getItems() {
        return countryService.findAll();
    }
    public void setItems(List<TblCountryOfOrigin> items) {
        this.items = items;
    }

    public List<TblCountryOfOrigin> getCountry()
    {
        return items = countryService.findAll();
    }

    @RequestMapping("/Country/View")
    public String list() {
        items = countryService.findAll();
        System.out.println(items + "Data");
        return "forward:/Country/Countryview.xhtml";
    }

    @RequestMapping("/Country/new")
    public String newBank() {
        selected = new TblCountryOfOrigin();
        int some = 12;
        selected.setCreatedBy("Lidiyam");
        System.out.println("lidu ");
        return "forward:/Country/NewCountry.xhtml";
    }

    public void save() {
        System.out.println("test ");
        selected = new TblCountryOfOrigin();
        if (selected != null) {
            selected.setCountry(cname);
            selected.setCode(ccode);
            selected.setCreatedBy("lidiyam");
            System.out.println("calue " + selected);
            int some = 12;

            countryService.saveNew(selected);
            items = countryService.findAll();
            System.out.println("saved");
        } else
            System.out.println("error");

    }

    public void delete() {

        countryService.delete(selected);

    }

    
}