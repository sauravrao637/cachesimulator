
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class cachecache {
	static Scanner in = new Scanner(System.in);

	public static void main(String[] args) throws IOException {

		System.out.print("Enter input file path and name:");
		//String inFile = in.next();
		String inFile="cachesimulaotr\\src\\input.txt";
		File file = new File(inFile);
		BufferedReader brr = new BufferedReader(new FileReader(file));
		System.out.println();
		//if file is already binary uncomment below 2 comments
		//		BufferedReader br = new BufferedReader(new FileReader(file));
		//		String lfCPU=br.readLine();



		FileOutputStream outputStream = new FileOutputStream("cachesimulaotr\\src\\binaryinput.txt");
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
		BufferedWriter writer = new BufferedWriter(outputStreamWriter);
		String t=brr.readLine();

		System.out.println("Enter data size");
		int s=in.nextInt();//Enter data size
		int x=whatPower(s);

		while(t!=null) {
			if(t.charAt(0)=='#')writer.write(t);
			else {
				String[] lA=t.split(" ");
				writer.write(lA[0]);
				writer.write(" ");
				writer.write(htob(lA[1],x));
			}
			writer.newLine();
			t=brr.readLine();
		}
		writer.close();
		//comment 4 lines below if file is already binary
		String inFileBinary="cachesimulaotr\\src\\binaryinput.txt";
		File fileBinary = new File(inFileBinary);
		BufferedReader br = new BufferedReader(new FileReader(fileBinary));
		String lfCPU=br.readLine();

		System.out.println("Enter number of cache lines");
		int cl=in.nextInt();//Enter number of cache lines

		System.out.println("Enter block size");
		int b=in.nextInt();//Enter block size


		System.out.println("Enter Choice");
		System.out.println("1:Direct Mapping");
		System.out.println("2:Associative Mapping");
		System.out.println("3:Set Associative Mapping");
		System.out.println("4:L2 Direct Mapping");
		System.out.println("To Exit: everything else");
		int choice=in.nextInt();
		switch (choice) {
		case 1:
		{
			Cache cache=createCache(cl,b);
			while(lfCPU!=null)
			{
				if(lfCPU.length()!=0&&lfCPU.charAt(0)!='#') {
					String[] lineArray=lfCPU.split(" ");

					if(lineArray[0].contentEquals("R")) {
						read(cache,lineArray[1]);
					}
					if(lineArray[0].contentEquals("W")) {
						write(cache,lineArray[1]);
					}
				}
				lfCPU=br.readLine();
			}
			displayCache(cache);
		}
		break;
		case 2:
		{
			CacheAM cacheAM=createCacheAM(cl,b);
			while(lfCPU!=null)
			{
				if(lfCPU.length()!=0&&lfCPU.charAt(0)!='#') {
					String[] lineArray=lfCPU.split(" ");

					if(lineArray[0].contentEquals("R")) {
						readAM(cacheAM,lineArray[1],cl);
					}
					if(lineArray[0].contentEquals("W")) {
						writeAM(cacheAM,lineArray[1],cl);
					}
				}
				lfCPU=br.readLine();
			}
			displayCacheAM(cacheAM);
		}
		break;
		case 3:
		{
			System.out.println("Enter number of sets");
			int sets=in.nextInt();
			CacheSAM cacheSAM=createCacheSAM(cl,b,sets);
			while(lfCPU!=null)
			{
				if(lfCPU.length()!=0&&lfCPU.charAt(0)!='#') {
					String[] lineArray=lfCPU.split(" ");

					if(lineArray[0].contentEquals("R")) {
						readSAM(cacheSAM,lineArray[1]);
					}
					if(lineArray[0].contentEquals("W")) {
						writeSAM(cacheSAM,lineArray[1]);
					}
				}
				lfCPU=br.readLine();
			}

			displayCacheSAM(cacheSAM);
		}
		break;
		case 4:
			CacheL2 cacheML=createCacheL2(cl,b);
			while(lfCPU!=null)
			{
				if(lfCPU.length()!=0&&lfCPU.charAt(0)!='#') {
					String[] lineArray=lfCPU.split(" ");

					if(lineArray[0].contentEquals("R")) {
						readML(cacheML,lineArray[1]);
					}
					if(lineArray[0].contentEquals("W")) {
						readML(cacheML,lineArray[1]);
					}
				}
				lfCPU=br.readLine();
			}
			displayCacheML(cacheML);

		default:
			System.exit(0);
		}

	}

	private static void displayCacheML(CacheL2 cacheML) {
		// TODO Auto-generated method stub
		int i;
		String tag;
		if(cacheML != null)
		{        System.out.println("Level 2 Cache");
		for(i = 0; i < cacheML.cl1; i++)
		{
			tag =null;
			if(cacheML.blocks1[i].tag != null)
			{
				tag = cacheML.blocks1[i].tag;
			}
			System.out.print("Cache Line : " + i);
			if(tag!=null)System.out.print(" tag:" + tag);
			else System.out.print(" EMPTY ");
			if(cacheML.blocks1[i].content!=null)System.out.println(" content: "+btoh(cacheML.blocks1[i].content));
			else {
				System.out.println();
			}
		}
		System.out.println("CACHE HITS L2:"+cacheML.hits1+"\nCACHE MISSES L2:"+cacheML.misses1+"\nMEMORY READS L2: "+cacheML.reads1+"\nMEMORY WRITES L2: "+cacheML.writes1);
		System.out.println();
		System.out.println("Level 1 Cache");

		for(i = 0; i < cacheML.cl2; i++)
		{
			tag =null;
			if(cacheML.blocks2[i].tag != null)
			{
				tag = cacheML.blocks2[i].tag;
			}
			System.out.print("Cache Line : " + i);
			if(tag!=null)System.out.print(" tag:" + tag);
			else System.out.print(" EMPTY ");
			if(cacheML.blocks2[i].content!=null)System.out.println(" content: "+btoh(cacheML.blocks2[i].content));
			else System.out.println();
		}
		System.out.println("CACHE HITS L1: "+cacheML.hits2+"\nCACHE MISSES L1: "+cacheML.misses2+"\nMEMORY READS L1: "+cacheML.reads2+"\nMEMORY WRITES L1: "+cacheML.writes2);
		}
	}

	private static void writeL1(CacheL2 cacheML,String st) {
		int n=st.length();
		int w =whatPower(cacheML.b1); //block size = 2**w
		int p1=whatPower(cacheML.cl1); //number of bits to identify cache line
		Block block = cacheML.blocks1[Integer.parseInt(st.substring(n-w-p1,n-w),2)];
		if(block.valid == 1 && block.tag.contentEquals(st.substring(0, n-w-p1)))
		{
			//System.out.println("address already exist with tag: "+st.substring(0, n-w-p)+" cache line : "+Integer.parseInt(st.substring(n-w-p,n-w),2));
			cacheML.writes1++;
			block.dirty = 1;
			cacheML.hits1++;
		}
		else {
			cacheML.misses1++;
			cacheML.reads1++;
			cacheML.writes1++;
			block.dirty=1;
			block.valid=1;
			block.tag=st.substring(0, n-w-p1);
			block.content=st.substring(0, n-w);
		}

	}
	private static void writeL2(CacheL2 cacheML, String st) {
		// TODO Auto-generated method stub
		int n=st.length();
		int w =whatPower(cacheML.b2); //block size = 2**w
		int p2=whatPower(cacheML.cl2); //number of bits to identify cache line
		Block block = cacheML.blocks2[Integer.parseInt(st.substring(n-w-p2,n-w),2)];
		if(block.valid == 1 && block.tag.contentEquals(st.substring(0, n-w-p2)))
		{
			//System.out.println("address already exist with tag: "+st.substring(0, n-w-p)+" cache line : "+Integer.parseInt(st.substring(n-w-p,n-w),2));
			cacheML.writes2++;
			block.dirty = 1;
			cacheML.hits2++;
		}
		else {
			cacheML.misses2++;
			cacheML.reads2++;
			cacheML.writes2++;
			block.dirty=1;
			block.valid=1;
			block.tag=st.substring(0, n-w-p2);
			block.content=st.substring(0, n-w);
		}


	}

	private static void readML(CacheL2 cacheML, String st) {
		// TODO Auto-generated method stub
		Block block;
		int n=st.length();
		int w =whatPower(cacheML.b1); //block size = 2**w
		int p1=whatPower(cacheML.cl1); //number of bits to identify cache line
		int p2=p1-1;
		block=cacheML.blocks2[Integer.parseInt(st.substring(n-w-p2,n-w),2)];

		if(block.valid == 1 && block.tag.contentEquals(st.substring(0, n-w-p2)))
		{
			//System.out.println("address found in cache line:"+Integer.parseInt(st.substring(n-w-p,n-w),2)+" with tag: "+st.substring(0, n-w-p));
			cacheML.hits2++;
		}
		else {
			cacheML.misses2++;
			cacheML.reads2++;

			block=cacheML.blocks1[Integer.parseInt(st.substring(n-w-p1,n-w),2)];

			if(block.valid == 1 && block.tag.contentEquals(st.substring(0, n-w-p1)))
			{
				cacheML.hits1++;
				cacheML.blocks1[Integer.parseInt(st.substring(n-w-p1,n-w),2)]=new Block();
				writeL2(cacheML, st);

			}
			else {
				cacheML.misses1++;
				cacheML.reads1++;
				writeL1(cacheML, st);

			}

		}

		return;

	}

	private static CacheL2 createCacheL2(int cl, int b) {
		CacheL2 cache=new CacheL2(cl,b);
		int i;

		if(cl<=0) {
			System.out.println("Enter valid number of cache lines");
			return null;
		}

		if(b<=0) {
			System.out.println("Enter valid block size");
		}


		for(i=0;i<cache.cl1;i++) {
			cache.blocks1[i]=new Block();
		}
		for(i=0;i<cache.cl2;i++) {
			cache.blocks2[i]=new Block();
		}

		return cache;
	}

	private static void displayCacheSAM(CacheSAM cacheSAM) {
		// TODO Auto-generated method stub
		if(cacheSAM != null)
		{       for(int i=0;i<cacheSAM.blocks.length;i++) {
			System.out.println("SET: "+i);
			while(!cacheSAM.blocks[i].isEmpty()) {
				Block block=cacheSAM.blocks[i].poll();
				if(block!=null) {
					System.out.print(" tag:" + block.tag);
					System.out.println(" address of block:" + btoh(block.content));
				}
			}
		}

		System.out.println("CACHE HITS:"+cacheSAM.hits+"\nCACHE MISSES:"+cacheSAM.misses+"\nMEMORY READS: "+cacheSAM.reads+"\nMEMORY WRITES:"+cacheSAM.writes);
		}

	}

	private static void writeSAM(CacheSAM cacheSAM, String st) {
		// TODO Auto-generated method stub
		int n=st.length();
		int w =whatPower(cacheSAM.b); //block size = 2**w
		int p=whatPower(cacheSAM.sets);
		Queue<Block> blockQ = cacheSAM.blocks[Integer.parseInt(st.substring(n-w-p,n-w),2)];

		Block blocksQ[] = new Block[cacheSAM.blocks[Integer.parseInt(st.substring(n-w-p,n-w),2)].size()];
		cacheSAM.blocks[Integer.parseInt(st.substring(n-w-p,n-w),2)].toArray(blocksQ);
		Block block;
		for(int i=0;i<blocksQ.length;i++) {
			block=blocksQ[i];
			if(block==null)continue;
			if(block.valid == 1 && block.tag.contentEquals(st.substring(0, n-w)))
			{	
				//System.out.println("address already exist with tag:"+ st.substring(0, n-w)+" in set: "+Integer.parseInt(st.substring(n-w-p,n-w),2));
				cacheSAM.writes++;
				block.dirty = 1;
				cacheSAM.hits++;
				return;
			}
		}
		cacheSAM.misses++;
		cacheSAM.reads++;
		cacheSAM.writes++;
		Block e=new Block();
		e.dirty=1;
		e.tag=st.substring(0, n-w);
		e.content=st.substring(0, n-w);;
		e.valid=1;


		//System.out.println("address not exist in cache adding now with tag: "+e.tag+" in set: "+Integer.parseInt(st.substring(n-w-p,n-w),2));

		if(cacheSAM.blocks[Integer.parseInt(st.substring(n-w-p,n-w),2)].size()<(cacheSAM.cl/cacheSAM.sets)) {
			cacheSAM.blocks[Integer.parseInt(st.substring(n-w-p,n-w),2)].add(e);
		}
		else {
			cacheSAM.blocks[Integer.parseInt(st.substring(n-w-p,n-w),2)].remove();
			cacheSAM.blocks[Integer.parseInt(st.substring(n-w-p,n-w),2)].add(e);
		}


	}

	private static void readSAM(CacheSAM cacheSAM, String string) {
		// TODO Auto-generated method stub


	}

	private static void displayCacheAM(CacheAM cache) {
		// TODO Auto-generated method stub
		if(cache != null)
		{        while(!cache.blocks.isEmpty()) {
			Block block=cache.blocks.poll();
			if(block!=null) {
				System.out.print(" tag:" + block.tag);
				System.out.println(" address of block:" + btoh(block.content));
			}
		}

		System.out.println("CACHE HITS:"+cache.hits+"\nCACHE MISSES:"+cache.misses+"\nMEMORY READS: "+cache.reads+"\nMEMORY WRITES:"+cache.writes);
		}

	}

	private static void writeAM(CacheAM cacheAM, String st,int cl) {
		// TODO Auto-generated method stub

		int n=st.length();
		int w =whatPower(cacheAM.b); //block size = 2**w
		Block blocks[] = new Block[cacheAM.blocks.size()];
		cacheAM.blocks.toArray(blocks);
		Block block;
		for(int i=0;i<blocks.length;i++) {
			block=blocks[i];
			if(block==null)continue;
			if(block.valid == 1 && block.tag.contentEquals(st.substring(0, n-w)))
			{	
				//System.out.println("address already exist with tag:"+ st.substring(0, n-w));
				cacheAM.writes++;
				block.dirty = 1;
				cacheAM.hits++;
				return;
			}
		}
		cacheAM.misses++;
		cacheAM.reads++;
		cacheAM.writes++;
		Block e=new Block();
		e.dirty=1;
		e.tag=st.substring(0, n-w);
		e.content=st.substring(0, n-w);
		e.valid=1;

		//System.out.println("address not exist in cache adding now with tag: "+e.tag);

		if(cacheAM.blocks.size()<cl) {
			cacheAM.blocks.add(e);
		}
		else {
			cacheAM.blocks.remove();
			cacheAM.blocks.add(e);
		}
	}

	private static void readAM(CacheAM cacheAM, String st,int cl) {
		// TODO Auto-generated method stub
		Block block;
		int n=st.length();
		int w =whatPower(cacheAM.b); //block size = 2**w
		Block blocks[] = new Block[cacheAM.blocks.size()];
		cacheAM.blocks.toArray(blocks);
		for(int i=0;i<blocks.length;i++) {
			block=blocks[i];
			if(block.valid == 1 && block.tag.contentEquals(st.substring(0, n-w)))
			{	
				//System.out.println("address found with block address: "+block.content);
				cacheAM.hits++;
				return;
			}
		}
		//System.out.println("address not found with tag: "+st.substring(0, n-w));
		cacheAM.misses++;
		cacheAM.reads++;
		writeAM(cacheAM,st,cl);	
		return;

	}

	/* displayCache
	 *
	 * Prints out the values of each slot in the cache
	 * as well as the hit, miss, read, write, and size
	 * data.
	 *
	 * @param       cache       of type Cache 
	 *
	 * @return      void
	 */
	private static void displayCache(Cache cache) {
		int i;
		String tag;
		if(cache != null)
		{        
			for(i = 0; i < cache.cl; i++)
			{
				tag =null;
				if(cache.blocks[i].tag != null)
				{
					tag = cache.blocks[i].tag;
				}
				System.out.print("Cache Line : " + i);
				System.out.print(" tag:" + tag);
				System.out.println(" content: "+btoh(cache.blocks[i].content));
			}
			System.out.println("CACHE HITS:"+cache.hits+"\nCACHE MISSES:"+cache.misses+"\nMEMORY READS: "+cache.reads+"\nMEMORY WRITES:"+cache.writes);
		}
	}


	/* write
	 *
	 * Function that writes data to the cache. 
	 *
	 * @param       cache       target cache
	 * @param       st          binary address
	 *
	 * @return      void
	 */

	private static void write(Cache cache, String st) {
		// TODO Auto-generated method stub
		int n=st.length();
		int w =whatPower(cache.b); //block size = 2**w
		int p=whatPower(cache.cl); //number of bits to identify cache line
		Block block = cache.blocks[Integer.parseInt(st.substring(n-w-p,n-w),2)];
		if(block.valid == 1 && block.tag.contentEquals(st.substring(0, n-w-p)))
		{
			//System.out.println("address already exist with tag: "+st.substring(0, n-w-p)+" cache line : "+Integer.parseInt(st.substring(n-w-p,n-w),2));
			cache.writes++;
			block.dirty = 1;
			cache.hits++;
		}
		else {
			cache.misses++;
			cache.reads++;
			cache.writes++;
			block.dirty=1;
			block.valid=1;
			block.tag=st.substring(0, n-w-p);
			block.content=st.substring(0, n-w);
			//System.out.println("address not found adding in cache line "+Integer.parseInt(st.substring(n-w-p,n-w),2)+" with tag: "+block.tag);

		}


	}

	/* read
	 *
	 * Function that reads data from a cache.
	 *
	 * @param       cache       target cache
	 * @param       st          binary address
	 *
	 * @return      void
	 */

	private static void read(Cache cache, String st) {
		// TODO Auto-generated method stub
		Block block;
		int n=st.length();
		int w =whatPower(cache.b); //block size = 2**w
		int p=whatPower(cache.cl); //number of bits to identify cache line
		block=cache.blocks[Integer.parseInt(st.substring(n-w-p,n-w),2)];

		if(block.valid == 1 && block.tag.contentEquals(st.substring(0, n-w-p)))
		{
			//System.out.println("address found in cache line:"+Integer.parseInt(st.substring(n-w-p,n-w),2)+" with tag: "+st.substring(0, n-w-p));
			cache.hits++;
		}
		else {
			cache.misses++;
			cache.reads++;
			if(block.dirty == 1)
			{
				block.dirty = 0;
			}
			block.valid=1;
			block.tag=st.substring(0, n-w-p);
			block.content=st.substring(0, n-w);
			//System.out.println("address not found writing in cache line:"+Integer.parseInt(st.substring(n-w-p,n-w),2)+" with tag: "+block.tag);
		}

		return;


	}


	//To find what power of 2 is a
	static int whatPower(int a) {
		if(a==1)return 0;
		if(a==2)return 1;
		return 1+whatPower(a/2);
	}

	/* createCache
	 *
	 * Function to create a new cache.
	 *
	 * @param   cl      number of cache lines
	 * @param   b      size of each block in bytes
	 * 
	 * @return  success         new Cache
	 * @return  failure         NULL
	 */

	private static Cache createCache(int cl, int b) {
		// TODO Auto-generated method stub
		Cache cache=new Cache(cl,b);
		int i;

		if(cl<=0) {
			System.out.println("Enter valid number of cache lines");
			return null;
		}

		if(b<=0) {
			System.out.println("Enter valid block size");
		}

		cache.cl=cl;
		cache.b=b;

		for(i=0;i<cl;i++) {
			cache.blocks[i]=new Block();
		}
		return cache;
	}

	private static CacheAM createCacheAM(int cl,int b) {
		// TODO Auto-generated method stub
		CacheAM cacheAM=new CacheAM(cl,b);
		int i;

		if(cl<=0) {
			System.out.println("Enter valid number of cache lines");
			return null;
		}

		if(b<=0) {
			System.out.println("Enter valid block size");
		}

		cacheAM.cl=cl;
		cacheAM.b=b;


		return cacheAM;
	}

	private static CacheSAM createCacheSAM(int cl,int b,int sets) {
		// TODO Auto-generated method stub
		CacheSAM cacheSAM=new CacheSAM(cl,b,sets);
		int i;

		if(cl<=0) {
			System.out.println("Enter valid number of cache lines");
			return null;
		}

		if(b<=0) {
			System.out.println("Enter valid block size");
		}

		if(sets<=0) {
			System.out.println("Enter valid number of sets");
		}

		cacheSAM.cl=cl;
		cacheSAM.b=b;
		cacheSAM.sets=sets;




		return cacheSAM;
	}

	static String htob(String str, int x)
	{	
		int dec;
		str=str.toLowerCase();
		dec=Integer.parseUnsignedInt(str,16);
		String bin=Integer.toBinaryString(dec);;
		String st="";
		for(int i=bin.length();i<x;i++)st+="0";

		return st+bin; 
	}

	static String btoh(String str) {
		int dec;
		dec=Integer.parseInt(str,2);
		return Integer.toHexString(dec);
	}
}


/* Block
 *
 * Holds an integer that states the validity of the bit (0 = invalid,
 * 1 = valid), the tag being held, and another integer that states if
 * the bit is dirty or not (0 = clean, 1 = dirty).
 */

class Block{
	int valid=0;
	String tag=null;
	int dirty=0;
	String content=null;
}


/* Cache
 *
 * Cache object that holds all the data about cache access as well as 
 * the write policy, sizes, and an array of blocks.
 *
 * @param   hits            # of cache accesses that hit valid data
 * @param   misses          # of cache accesses that missed valid data
 * @param   reads           # of reads from main memory
 * @param   writes          # of writes from main memory
 * @param   cl              # of cache lines
 * @param   b               How big each block of data should be
 * @param   blocks          The actual array of blocks  
 */

class Cache {
	int hits=0;
	int misses=0;
	int reads=0;
	int writes=0;
	int cl;
	int b;
	Block[] blocks; 

	Cache(int cl,int b){
		this.blocks=new Block[cl];
		this.cl=cl;
		this.b=b;
	}




}


class CacheAM{
	int hits=0;
	int misses=0;
	int reads=0;
	int writes=0;
	int cl;
	int b;
	Queue<Block> blocks;


	CacheAM(int cl,int b){
		this.blocks=new LinkedList<>();
		this.cl=cl;
		this.b=b;
	}
}


class CacheSAM{
	int hits=0;
	int misses=0;
	int reads=0;
	int writes=0;
	int cl;
	int b;
	int sets;
	Queue<Block>[] blocks;

	CacheSAM(int cl,int b,int sets){
		this.blocks=new LinkedList[cl/sets];
		for(int i = 0; i < cl/sets; i++) 
			this.blocks[i] = new LinkedList<Block>(); 

		this.cl=cl;
		this.b=b;
	}
}


class CacheL2{
	int hits1=0;
	int misses1=0;
	int reads1=0;
	int writes1=0;
	int cl1;
	int b1;
	Block[] blocks1; 

	int hits2=0;
	int misses2=0;
	int reads2=0;
	int writes2=0;
	int cl2;
	int b2;
	Block[] blocks2; 

	CacheL2(int cl,int b){
		this.blocks1=new Block[cl];
		this.blocks2=new Block[cl/2];
		this.cl1=cl;
		this.cl2=cl/2;
		this.b1=b;
		this.b2=b;
	}

}

//As per info taken from https://www.ntu.edu.sg/home/smitha/ParaCache/Paracache/dmc.html