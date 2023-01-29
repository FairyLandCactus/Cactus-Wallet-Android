package com.qianlihu.solanawallet.rpc.type;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * author : Duan
 * date   : 2021/12/2317:38
 * desc   :
 * version: 1.0.0
 */
public class RpcFilters {

//    @Json(name = "filters")
    private List<FiltersBean> filters;
//    @Json(name = "encoding")
    private String encoding;

    public List<FiltersBean> getFilters() {
        return filters;
    }

    public void setFilters(List<FiltersBean> filters) {
        this.filters = filters;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public static class FiltersBean {
        /**
         * dataSize : 17
         * memcmp : {"offset":4,"bytes":"3Mc6vR"}
         */

        public static class DataSizeBean extends FiltersBean {
//            @Json(name = "dataSize")
            private long dataSize;

            public long getDataSize() {
                return dataSize;
            }

            public void setDataSize(long dataSize) {
                this.dataSize = dataSize;
            }

            @Override
            public String toString() {
                return "DataSizeBean{" +
                        "dataSize=" + dataSize +
                        '}';
            }
        }

        public static class MemcmpBean extends FiltersBean {

//            @Json(name = "memcmp")
            private MMemcmp memcmp;

            public void setMemcmp(MMemcmp memcmp) {
                this.memcmp = memcmp;
            }

            public static class MMemcmp{
                /**
                 * offset : 4
                 * bytes : 3Mc6vR
                 */
//                @Json(name = "offset")
                private long offset;
//                @Json(name = "bytes")
                private String bytes;

                public long getOffset() {
                    return offset;
                }

                public void setOffset(long offset) {
                    this.offset = offset;
                }

                public String getBytes() {
                    return bytes;
                }

                public void setBytes(String bytes) {
                    this.bytes = bytes;
                }

                @Override
                public String toString() {
                    return "MMemcmp{" +
                            "offset=" + offset +
                            ", bytes='" + bytes + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "MemcmpBean{" +
                        "memcmp=" + memcmp +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "RpcFilters{" +
                "filters=" + filters +
                ", encoding='" + encoding + '\'' +
                '}';
    }
}
