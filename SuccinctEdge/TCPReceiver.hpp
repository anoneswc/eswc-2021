#ifndef SUCCINCTEDGE_CATA_TCPRECEIVER_HPP
#define SUCCINCTEDGE_CATA_TCPRECEIVER_HPP

// Server side C/C++ program to demonstrate Socket programming
#include <unistd.h>
#include <cstdio>
#include <sys/socket.h>
#include <cstdlib>
#include <netinet/in.h>
#include <iostream>
#include <thread>
#include <vector>
#include <regex>
#include <mutex>
#include <fstream>

#define PORT 25005

//using namespace std;

extern std::vector<std::string> app_buff;
extern bool interrupt_flag;
extern std::thread receiver;
extern std::vector<std::string> columns;
extern std::vector<std::vector<std::string>> content;
extern std::mutex lock_tcp;
extern long long time_reception;

std::vector<std::string> split(const std::string& input, const std::string& regex);

void tcpReceiver();

std::vector<std::vector<std::string>> getCSV();

// Lance la Thread qui reçoit les données par TCP
void run_tcp_receiver();

// Arrête la thread TCP
void stop_tcp_receiver();

std::string erase_head(std::string str);



#endif //SUCCINCTEDGE_CATA_TCPRECEIVER_HPP



// Server side C/C++ program to demonstrate Socket programming
