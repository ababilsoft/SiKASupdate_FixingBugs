package com.awandigital.sikas.utils;


public class AppConf {
    public static final String BASE_URL = "https://ababilproject.com/";
    private static final String BASE_INDEX = BASE_URL + "api_siqasir/index.php/";
    public static final String URL_REG_TOKO = BASE_INDEX + "RegistrasiToko/tambahkan";
    public static final String URL_UPDATE_TOKO = BASE_INDEX + "RegistrasiToko/update";
    public static final String URL_KODE_SAAT_INI = BASE_INDEX + "RegistrasiToko/get_kode_toko";
    public static final String URL_GET_DATA__PROVINSI = BASE_INDEX + "DataWilayah/data_provinsi";
    public static final String URL_GET_DATA_KOTA = BASE_INDEX + "DataWilayah/kota_by_prov_id";
    public static final String httpTag = "KillHttp";
}
