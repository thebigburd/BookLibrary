export interface Book {
	id: number; // Optional, as auto generated?
	name: string;
	description: string;
	publishYear: number;
	currentStock: number;
	totalStock: number;
	status: BookStatus;
}
  
  // Define the BookStatus type (from the Book model class)
  export enum BookStatus {
	AVAILABLE = "AVAILABLE",
	UNAVAILABLE = "UNAVAILABLE",
  }
  
