package com.zylos.backend.features.course.material.exceptions;

public class MaterialNotFoundException extends RuntimeException {
    public MaterialNotFoundException(Long id) {
        super("Material with ID " + id + " not found.");
    }
}