cmake_minimum_required(VERSION 3.17)
project(SuccinctEdge_cata)

set(CMAKE_CXX_STANDARD 14)


include_directories(/Users/xu/include)
link_directories(/Users/xu/lib)
link_libraries(sdsl divsufsort divsufsort64)
add_executable(SuccinctEdge_cata main.cpp TripleStore.cpp TripleStore.hpp RDFStorage.cpp RDFStorage.hpp Dictionary.cpp Dictionary.hpp RankBaseElement.cpp RankBaseElement.hpp Util.hpp RDFTypeStore.cpp RDFTypeStore.hpp Constant.hpp quickSortTS.hpp TriplePattern.cpp TriplePattern.hpp LiteMatDictionary.cpp LiteMatDictionary.hpp JoinLine.cpp JoinLine.hpp JoinVariables.cpp JoinVariables.hpp JoinTable.cpp JoinTable.hpp QueryEngine.cpp QueryEngine.hpp BitMap.cpp BitMap.hpp DataTypeStore.cpp DataTypeStore.hpp PropertyLiteMatDictionary.cpp PropertyLiteMatDictionary.hpp StrDataRankBaseElement.cpp StrDataRankBaseElement.hpp DataSeries.cpp DataSeries.hpp DataBlock.cpp DataBlock.hpp TCPReceiver.cpp TCPReceiver.hpp ShowResults.hpp DataWindowUtil.hpp MiniBatchUtil.hpp TimeUtil.hpp)

SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -pthread")