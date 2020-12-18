//
// Created by weiqin xu on 19/11/2020.
//

#include "TCPReceiver.hpp"

std::vector<std::string> app_buff;
bool interrupt_flag = false;
std::thread receiver;
std::vector<std::string> columns;
std::vector<std::vector<std::string>> content;
std::mutex lock_tcp;
long long time_reception;


std::vector<std::string> split(const std::string& input, const std::string& regex) {
    std::regex re(regex);
    std::sregex_token_iterator
            first{input.begin(), input.end(), re, -1},
            last;
    return {first, last};
}

void tcpReceiver(){
    int server_fd, new_socket, valread;
    struct sockaddr_in address;
    int opt = 1;
    int addrlen = sizeof(address);
    char buffer[1024] = {0};

    // Creating socket file descriptor
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0)
    {
        perror("socket failed");
        exit(EXIT_FAILURE);
    }

    // Forcefully attaching socket to the port 8080
    if (setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR,
                   &opt, sizeof(opt)))
    {
        perror("setsockopt");
        exit(EXIT_FAILURE);
    }
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = INADDR_ANY;
    address.sin_port = htons( PORT );

    // Forcefully attaching socket to the port 8080
    if (bind(server_fd, (struct sockaddr *)&address,
             sizeof(address))<0)
    {
        perror("bind failed");
        exit(EXIT_FAILURE);
    }
    if (listen(server_fd, 3) < 0)
    {
        perror("listen");
        exit(EXIT_FAILURE);
    }
    if ((new_socket = accept(server_fd, (struct sockaddr *)&address,
                             (socklen_t*)&addrlen))<0)
    {
        perror("accept");
        exit(EXIT_FAILURE);
    }
    std::string rest_row = std::string();
    char buffer_concat[2048];
    while(!interrupt_flag){
        lock_tcp.lock();
        memset(buffer,0,1024);
        valread = read( new_socket , buffer, 1023);
        //std::cout << "buffer length:" << valread << std::endl;
        //buffer[valread] = '\0';

        /*
        for(auto i:buffer){
            std::cout << i;
        }
        */

        strcpy(buffer_concat,rest_row.c_str());
        strcat(buffer_concat,buffer);

        std::vector<std::string> row = split(buffer_concat, "\n");
        //std::cout << "begin buffer " << buffer_concat << " end buffer" << std::endl;

        //std::cout << buffer_concat[strlen(buffer_concat)-1] << std::endl;
        bool end_of_line = (buffer_concat[strlen(buffer_concat)-1] == '\n');
        //std::cout << "row size: " << row.size() << std::endl;
        //To see the condition
        if(!end_of_line ){
            rest_row = row.back();
            row.pop_back();
        }else{
            rest_row = "";
        }

        if (columns.empty()) {
            // First row received defines the column names
            // We still have to add it to the content
            columns = split(row.front(), ",");
            //std::cout << row.front() << std::endl;
            for(auto i = 1; i < row.size(); ++i){
                content.push_back(split(row[i], ","));
            }
        }else{
            for(auto i = 0; i < row.size(); ++i){
                content.push_back(split(row[i], ","));
            }

        }
        // Other rows are content

        lock_tcp.unlock();

        //sleep(1);
        usleep(10);
    }
}

std::vector<std::vector<std::string>> getCSV(){
    lock_tcp.lock();
    // No data has been received yet
    if(columns.empty())
        return {};

    std::vector<std::vector<std::string>> tmp = content;

    //std::cout << "tmp size: " << tmp.size() << std::endl;

    // Reset column names for next rows
    content = {};

    for(auto i:tmp){
        if(i[0][1] == 'e'){
            time_reception = std::chrono::system_clock::now().time_since_epoch() /std::chrono::milliseconds(1);
            break;
        }
    }

    lock_tcp.unlock();
    return tmp;
}


// Lance la Thread qui reçoit les données par TCP
void run_tcp_receiver(){
    receiver = std::thread (tcpReceiver);
}

// Arrête la thread TCP
void stop_tcp_receiver(){
    interrupt_flag = true;
    receiver.join();
}

std::string erase_head(std::string str){
    //std::cout << "current str:" << str << " ";
    while(str.size()>0 && !(str.front() >= '0' && str.front() <= '9'))
        str.erase(str.begin());
    //std::cout << str << " ";
    return str;
}