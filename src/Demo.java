AsyncLoaderUtil.getAsynLoader().submitTask(this, new AsyncLoadRunnable(new AsyncLoadRunnable.AsyncHandlerInterface() {

@Override
public void sendFinishMessage() {
//��ɵ��ú�UI����ʾ
        tvLoading.setVisibility(View.INVISIBLE);
        lvContact.setVisibility(View.VISIBLE);
        lvContact.setAdapter(adapter);
}

@Override
public void sendCancelMessage() {

}

@Override
public void loadingData() {
//���ؼ��غ�ʱ����

        contactsList.addAll(getPhoneContacts());
        contactsList.addAll(getSIMContacts());
        Collections.sort(contactsList,new Comparator<Person>() {
            @Override
                public int compare(Person lhs, Person rhs) {

                return lhs.getFirstAlphabet().compareToIgnoreCase(rhs.getFirstAlphabet());
            }
        }); 
    }
}));