select *
  from s_post;

insert into s_post(id, board_id, writer_id, title, content);			 
select get_id(seq_post_id.nextval), board_id, writer_id, title, content
  from s_post;
  
select *
  from s_post order by id;

  select *
  from s_post order by id || 'rrr';
  
  
  select h.id
    from s_hashtag h
   where hashtag = '객체';
   
   
   delete from sm_ht2post
    from hashtag = 4;
   
    
  select h.id, p.id, 1
    from s_hashtag h, s_post p
   where h.hashtag = '객체';
   
   
   insert into sm_ht2party(hashtag_id, user_id, occur_cnt)
   select h.id, p.id, 1
    from s_hashtag h, s_post p
   where h.hashtag = '객체';
   
   
create table sm_ht2party(
	hashtag_id		number(9),
	user_id, 		varchar2(10),
	occur_cnt		number(9),