package com.fh.clients.envi;

public interface IEnvService {
    String[] requestEnvironmentDataTypes();

    EnvData requestEnvironmentData(String _type);

    EnvData[] requestAll();
}
