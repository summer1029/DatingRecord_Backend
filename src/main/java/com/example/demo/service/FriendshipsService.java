package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.Member;

import dto.FriendListDto;


public interface FriendshipsService {

	void sendFriendRequest(Member requester, Member addressee);

	void acceptFriendRequest( Member requester,Member addressee);

	void deleteFriend(Member requester, Member addressee);

	List<FriendListDto> getMyFriends(String myEmail);




	
}
