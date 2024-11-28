package com.aluracursos.desafio_literalura.service;

public interface ITransformData {
    <T> T getData(String json, Class<T> tClass);
}
