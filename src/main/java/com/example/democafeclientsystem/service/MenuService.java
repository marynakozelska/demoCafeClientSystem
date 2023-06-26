package com.example.democafeclientsystem.service;

import com.example.democafeclientsystem.repositories.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuItemService {
    private final MenuRepository repository;


}
