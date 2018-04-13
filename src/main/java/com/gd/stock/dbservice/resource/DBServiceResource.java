package com.gd.stock.dbservice.resource;


import com.gd.stock.dbservice.model.Quote;
import com.gd.stock.dbservice.model.Quotes;
import com.gd.stock.dbservice.repository.QuotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/db")
public class DBServiceResource {

    @Autowired
    private QuotesRepository quotesRepository;

    @GetMapping("/{username}")
    List<String>  getQuotes(@PathVariable("username")
                             final String username){
        return findQuotesByUserName(username);
    }

    private List<String> findQuotesByUserName(@PathVariable("username") String username) {
        return this.quotesRepository.findByUserName(username)
                .stream()
                .map(Quote::getQuote)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    List<String> add(@RequestBody final Quotes quotes){
        quotes.getQuotes()
                .stream()
                .map(quote -> new Quote(quotes.getUserName(),quote))
                .forEach(quote -> {
                    quotesRepository.save(quote);
                });
        return findQuotesByUserName(quotes.getUserName());
    }

    @PostMapping("/delete/{username}")
    List<String> delete(@PathVariable("username") final String username){
        List<Quote> quotes = this.quotesRepository.findByUserName(username);
        quotesRepository.deleteAll(quotes);
        return findQuotesByUserName(username);
    }
}
